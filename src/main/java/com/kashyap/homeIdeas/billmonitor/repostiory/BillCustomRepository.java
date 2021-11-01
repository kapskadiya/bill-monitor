package com.kashyap.homeIdeas.billmonitor.repostiory;

import com.kashyap.homeIdeas.billmonitor.constant.BillType;
import com.kashyap.homeIdeas.billmonitor.constant.TimeInterval;
import com.kashyap.homeIdeas.billmonitor.model.Bill;
import com.kashyap.homeIdeas.billmonitor.model.ChartValue;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Kashyap Kadiya
 * @since 2021-06
 */
public interface BillCustomRepository {
    boolean bulkInsert(List<Bill> billList) throws IOException;

    void updateIsDeleted(String id, boolean isDeleted) throws IOException;

    String findOnlyESIdByBillId(String billId) throws IOException;

    List<String> findOnlyESIdsByCustomerId(String customerId) throws IOException;

    void bulkPartialUpdate(List<String> esIdList, String field, String value) throws IOException;

    List<ChartValue> findAmountAndTimeByAgg(BillType billType, TimeInterval timeInterval) throws IOException;

    List<Map<String, Object>> findMaxAmountPerYear(BillType billType) throws IOException;

    List<Map<String, Object>> findMinAmountPerYear(BillType billType) throws IOException;

    Double findTotalAmountByType(BillType billType) throws IOException;

    Map<String, Map<String, Double>> findTotalAmountPerTypePerYear() throws IOException;

    List<ChartValue> findTotalAmountPerType() throws IOException;

    List<Map<String, Object>> findUnPaidBillsByType(BillType billType) throws IOException;
}
