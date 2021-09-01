package com.kashyap.homeIdeas.billmonitor.service;

import com.kashyap.homeIdeas.billmonitor.model.Bill;
import com.kashyap.homeIdeas.billmonitor.model.BillType;

import java.io.IOException;
import java.util.List;

public interface BillService {

    String save(Bill bill);

    boolean isExist(String billId);

    Bill getById(String billId);

    List<Bill> getByCustomerId(String customerId);

    List<Bill> getByCustomerName(String customerName);

    String update(Bill newBill);

    void remove(String billId);

    List<Bill> getAll();

    boolean bulkSave(List<Bill> billList) throws IOException;

    List<Bill> getByType(BillType type);
}
