package com.kashyap.homeIdeas.billmonitor.repostiory.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kashyap.homeIdeas.billmonitor.model.Bill;
import com.kashyap.homeIdeas.billmonitor.repostiory.BillCustomRepository;
import com.kashyap.homeIdeas.billmonitor.repostiory.NoSQLOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
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

    private ObjectMapper getDefaultObjectMapper(){
        final ObjectMapper ob = new ObjectMapper();
        ob.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        ob.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        return ob;
    }

}
