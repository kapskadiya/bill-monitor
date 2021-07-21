package com.kashyap.homeIdeas.billmonitor.service;

import com.kashyap.homeIdeas.billmonitor.model.Bill;

public interface BillService {

    String save(Bill bill);

    boolean isExist(String billId);
}
