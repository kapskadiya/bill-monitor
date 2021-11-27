package com.kashyap.homeIdeas.billmonitor.service;

import com.kashyap.homeIdeas.billmonitor.model.BillType;

import java.util.List;

public interface BillTypeService {
    BillType getBillType(String billType);

    List<BillType> getAllBillType();

    void save(BillType billType);
}
