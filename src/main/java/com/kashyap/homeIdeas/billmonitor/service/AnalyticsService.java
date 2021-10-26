package com.kashyap.homeIdeas.billmonitor.service;

import com.kashyap.homeIdeas.billmonitor.constant.BillType;
import com.kashyap.homeIdeas.billmonitor.constant.TimeInterval;
import com.kashyap.homeIdeas.billmonitor.model.ChartValue;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface AnalyticsService {
    List<ChartValue> getAmountAndTime(BillType billType, TimeInterval timeIn) throws IOException;

    List<Map<String, Object>> getMaxAmountPerYear(BillType billType) throws IOException;
}
