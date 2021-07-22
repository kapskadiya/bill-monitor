package com.kashyap.homeIdeas.billmonitor.service;

import com.kashyap.homeIdeas.billmonitor.model.Bill;

import java.util.List;

public interface BillService {

    String save(Bill bill);

    boolean isExist(String billId);

    Bill getById(String billId);

    List<Bill> getByCustomerId(String customerId);

    List<Bill> getByCustomerName(String customerName);

    String update(Bill newBill);

    void remove(String billId);
}
