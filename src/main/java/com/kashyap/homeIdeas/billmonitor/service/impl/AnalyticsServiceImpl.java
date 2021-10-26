package com.kashyap.homeIdeas.billmonitor.service.impl;

import com.kashyap.homeIdeas.billmonitor.constant.BillType;
import com.kashyap.homeIdeas.billmonitor.constant.TimeInterval;
import com.kashyap.homeIdeas.billmonitor.model.ChartValue;
import com.kashyap.homeIdeas.billmonitor.repostiory.BillRepository;
import com.kashyap.homeIdeas.billmonitor.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        return billRepository.findMaxAmountPerYear(billType);
    }

}
