package com.kashyap.homeIdeas.billmonitor.service;

import com.kashyap.homeIdeas.billmonitor.model.Bill;
import com.kashyap.homeIdeas.billmonitor.model.BillType;

import java.io.IOException;
import java.util.List;

/**
 * @author Kashyap Kadiya
 * @since 2021-06
 */
public interface BillService {

    void save(Bill bill);

    boolean isExist(String billId);

    Bill getById(String billId);

    Bill getByBillId(String billId);

    List<Bill> getByCustomerId(String customerName);

    void update(Bill newBill);

    void deleteById(String billId) throws IOException;

    void deleteByCustomerId(String customerId) throws IOException;

    void deleteByESId(String id);

    List<Bill> getAll();

    boolean bulkSave(List<Bill> billList) throws IOException;

    List<Bill> getByType(BillType type);

    List<Bill> getByIsDeleted(boolean isDeleted);

}
