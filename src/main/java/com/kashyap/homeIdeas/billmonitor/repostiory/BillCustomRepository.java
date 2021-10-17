package com.kashyap.homeIdeas.billmonitor.repostiory;

import com.kashyap.homeIdeas.billmonitor.model.Bill;

import java.io.IOException;
import java.util.List;

public interface BillCustomRepository {
    boolean bulkInsert(List<Bill> billList) throws IOException;

    void updateIsDeleted(String id, boolean isDeleted) throws IOException;

    String getOnlyESIdByBillId(String billId) throws IOException;

    List<String> getOnlyESIdsByCustomerId(String customerId) throws IOException;

    void bulkPartialUpdate(List<String> esIdList, String field, String value) throws IOException;
}
