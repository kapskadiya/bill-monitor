package com.kashyap.homeIdeas.billmonitor.service.impl;

import com.kashyap.homeIdeas.billmonitor.model.BillType;
import com.kashyap.homeIdeas.billmonitor.constant.TimeInterval;
import com.kashyap.homeIdeas.billmonitor.model.ChartValue;
import com.kashyap.homeIdeas.billmonitor.repostiory.BillRepository;
import com.kashyap.homeIdeas.billmonitor.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kashyap.homeIdeas.billmonitor.constant.ApplicationConstant.BILL_ID;
import static com.kashyap.homeIdeas.billmonitor.constant.ApplicationConstant.DUE_DATE;
import static com.kashyap.homeIdeas.billmonitor.constant.ApplicationConstant.ISSUE_DATE;
import static com.kashyap.homeIdeas.billmonitor.constant.ApplicationConstant.TOTAL_AMOUNT;

/**
 * @author Kashyap Kadiya
 * @since 2021-06
 */
@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    @Autowired
    private BillRepository billRepository;

    @Override
    public List<ChartValue> getAmountAndTime(BillType billType, TimeInterval timeIn) throws IOException {
        return billRepository.findAmountAndTimeByAgg(billType, timeIn);
    }

    @Override
    public List<Map<String, Object>> getMaxAmountPerYear(BillType billType) throws IOException {
        final List<Map<String, Object>> dataList = billRepository.findMaxAmountPerYear(billType);

        dataList.forEach(dataMap ->
            dataMap.computeIfPresent(ISSUE_DATE,(k,v) -> v = new Date(Long.parseLong(v.toString()))));

        return dataList;
    }

    @Override
    public List<Map<String, Object>> getMinAmountPerYear(BillType billType) throws IOException {
        final List<Map<String, Object>> dataList = billRepository.findMinAmountPerYear(billType);

        dataList.forEach(dataMap ->
                dataMap.computeIfPresent(ISSUE_DATE,(k,v) -> v = new Date(Long.parseLong(v.toString()))));

        return dataList;
    }

    @Override
    public Map<String, Map<String, Double>> getAmountPerTypePerYear() throws IOException {
        return billRepository.findTotalAmountPerTypePerYear();
    }

    @Override
    public Double getTotalAmountSoFar(BillType billType) throws IOException {
        return billRepository.findTotalAmountByType(billType);
    }

    @Override
    public List<ChartValue> getTotalAmountPerType() throws IOException {
        return billRepository.findTotalAmountPerType();
    }

    @Override
    public List<Map<String, Object>> getUnpaidBillsByType(BillType billType) throws IOException {
        final List<Map<String, Object>> billList = billRepository.findUnPaidBillsByType(billType);
        final String[] keys = new String[]{BILL_ID, TOTAL_AMOUNT, ISSUE_DATE, DUE_DATE};

        final List<Map<String, Object>> dataList = new ArrayList<>();
        billList.forEach(bill -> {
            final Map<String, Object> dataMap = new HashMap<>();
            for (String key : keys) {
                dataMap.computeIfAbsent(key, bill::get);
            }
            dataList.add(dataMap);
        });

        return dataList;
    }
}
