package com.kashyap.homeIdeas.billmonitor.service.impl;

import com.kashyap.homeIdeas.billmonitor.model.Bill;
import com.kashyap.homeIdeas.billmonitor.model.BillType;
import com.kashyap.homeIdeas.billmonitor.service.AnalyticsService;
import com.kashyap.homeIdeas.billmonitor.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    @Autowired
    private BillService billService;

    @Override
    public Map<String, Integer> getMonthAndAmount(BillType billType) {

        final List<Bill> billList = billService.getByType(billType);

        final Map<String, Integer> monthAndAmountMap = new HashMap<>();

        billList.forEach(bill -> {
            final Date issueDate = bill.getIssueDate();
            final LocalDate localDate = issueDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            final String monthName = localDate.getMonth().name();

            monthAndAmountMap.put(monthName, bill.getTotalAmount());
        });

        return monthAndAmountMap;

    }
}
