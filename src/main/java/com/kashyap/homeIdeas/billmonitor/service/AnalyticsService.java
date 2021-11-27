package com.kashyap.homeIdeas.billmonitor.service;

import com.kashyap.homeIdeas.billmonitor.model.BillType;
import com.kashyap.homeIdeas.billmonitor.constant.TimeInterval;
import com.kashyap.homeIdeas.billmonitor.model.ChartValue;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Kashyap Kadiya
 * @since 2021-06
 */
public interface AnalyticsService {
    List<ChartValue> getAmountAndTime(BillType billType, TimeInterval timeIn) throws IOException;

    List<Map<String, Object>> getMaxAmountPerYear(BillType billType) throws IOException;

    List<Map<String, Object>> getMinAmountPerYear(BillType billType) throws IOException;

    Map<String, Map<String, Double>> getAmountPerTypePerYear() throws IOException;

    Double getTotalAmountSoFar(BillType billType) throws IOException;

    List<ChartValue> getTotalAmountPerType() throws IOException;

    List<Map<String, Object>> getUnpaidBillsByType(BillType billType) throws IOException;
}
