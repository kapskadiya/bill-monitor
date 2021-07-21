package com.kashyap.homeIdeas.billmonitor.service.impl;

import com.kashyap.homeIdeas.billmonitor.model.Bill;
import com.kashyap.homeIdeas.billmonitor.repostiory.BillRepository;
import com.kashyap.homeIdeas.billmonitor.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private BillRepository repository;

    @Override
    public String save(Bill bill) {
        final Bill savedBill = repository.save(bill);

        return bill.getId();
    }

    @Override
    public boolean isExist(String billId) {
        final Optional<Bill> bill = repository.findById(billId);
        return bill.isPresent();
    }

}
