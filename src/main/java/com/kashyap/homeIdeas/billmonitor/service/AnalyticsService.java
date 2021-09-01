package com.kashyap.homeIdeas.billmonitor.service;

import com.kashyap.homeIdeas.billmonitor.model.BillType;

import java.util.Map;

public interface AnalyticsService {
    Map<String, Integer> getMonthAndAmount(BillType billType);
}
