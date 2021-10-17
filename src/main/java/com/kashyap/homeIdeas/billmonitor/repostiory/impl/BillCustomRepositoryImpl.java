package com.kashyap.homeIdeas.billmonitor.repostiory.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kashyap.homeIdeas.billmonitor.model.Bill;
import com.kashyap.homeIdeas.billmonitor.repostiory.BillCustomRepository;
import com.kashyap.homeIdeas.billmonitor.repostiory.NoSQLOperations;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class BillCustomRepositoryImpl implements BillCustomRepository {

    @Autowired
    private NoSQLOperations noSQLOperations;

    private static final Logger log = LoggerFactory.getLogger(BillCustomRepositoryImpl.class);

    private final ObjectMapper objectMapper = getDefaultObjectMapper();
    private final String indexName = "bill";

    @Override
    public boolean bulkInsert(List<Bill> billList) throws IOException {

        final List<String> billInJsonList = billList.stream()
                .map(bill -> {
                    try {
                        return objectMapper.writeValueAsString(bill);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .collect(Collectors.toList());

         return noSQLOperations.bulkInsert(indexName, billInJsonList);
    }

    @Override
    public void updateIsDeleted(String id, boolean isDeleted) throws IOException {

       if(StringUtils.isBlank(id)) {
           return;
       }
        noSQLOperations.partialUpdate(indexName, id, "isDeleted", String.valueOf(isDeleted));
    }

    @Override
    public String getOnlyESIdByBillId(String billId) throws IOException {
        if (StringUtils.isBlank(billId)) {
            return StringUtils.EMPTY;
        }

        final List<String> idList = noSQLOperations.getOnlyIds(indexName, "billId", billId);
        if (CollectionUtils.isNotEmpty(idList) && StringUtils.isNotBlank(idList.get(0))) {
            return idList.get(0);
        }
        return StringUtils.EMPTY;
    }

    @Override
    public List<String> getOnlyESIdsByCustomerId(String customerId) throws IOException {
        if (StringUtils.isBlank(customerId)) {
            return new ArrayList<>();
        }

        final List<String> idList = noSQLOperations.getOnlyIds(indexName, "customerId", customerId);
        if (CollectionUtils.isNotEmpty(idList)) {
            return idList;
        }
        return new ArrayList<>();
    }

    @Override
    public void bulkPartialUpdate(List<String> esIdList, String field, String value) throws IOException {
        if (CollectionUtils.isEmpty(esIdList) || StringUtils.isBlank(field)) {
            return;
        }
        noSQLOperations.bulkUpdate(indexName, esIdList, field, value);
    }

    private ObjectMapper getDefaultObjectMapper(){
        final ObjectMapper ob = new ObjectMapper();
        ob.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        ob.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        return ob;
    }

}
