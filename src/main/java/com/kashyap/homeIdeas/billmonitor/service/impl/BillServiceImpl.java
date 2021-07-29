package com.kashyap.homeIdeas.billmonitor.service.impl;

import com.kashyap.homeIdeas.billmonitor.model.Bill;
import com.kashyap.homeIdeas.billmonitor.repostiory.BillRepository;
import com.kashyap.homeIdeas.billmonitor.service.BillService;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BillServiceImpl implements BillService {

    private static final Logger log = LoggerFactory.getLogger(BillServiceImpl.class);

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

    @Override
    public Bill getById(String billId) {
        if (StringUtils.isBlank(billId)) {
            log.error("BillId is empty");
            return new Bill();
        }
        final Optional<Bill> bill = repository.findById(billId);
        return bill.orElse(new Bill());
    }

    @Override
    public List<Bill> getByCustomerId(String customerId) {
        if (StringUtils.isBlank(customerId)) {
            log.error("CustomerId is empty");
            return new ArrayList<>();
        }
        return repository.findByCustomerId(customerId);
    }

    @Override
    public List<Bill> getByCustomerName(String customerName) {
        if (StringUtils.isBlank(customerName)) {
            log.error("CustomerName is empty");
            return new ArrayList<>();
        }
        return repository.findByCustomerName(customerName);
    }

    @Override
    public String update(Bill newBill) {
        if (newBill == null) {
            return StringUtils.EMPTY;
        }

        final Optional<Bill> existingBillOptional = repository.findById(newBill.getId());

        if (existingBillOptional.isPresent()) {
            final Bill updatedBill = existingBillOptional.get();
            if (StringUtils.isNotBlank(newBill.getOrgName())) {
                updatedBill.setOrgName(newBill.getOrgName());
            }
            if (StringUtils.isNotBlank(newBill.getCustomerName())) {
                updatedBill.setCustomerName(newBill.getCustomerName());
            }
            if (newBill.getType() != null) {
                updatedBill.setType(newBill.getType());
            }
            if (newBill.getAmountToBePay() != 0) {
                updatedBill.setAmountToBePay(newBill.getAmountToBePay());
            }
            if (newBill.getIssueDate() != null) {
                updatedBill.setIssueDate(newBill.getIssueDate());
            }
            if (newBill.getDueDate() != null) {
                updatedBill.setDueDate(newBill.getDueDate());
            }
            if (newBill.getBillingDurationInDays() != 0) {
                updatedBill.setBillingDurationInDays(newBill.getBillingDurationInDays());
            }

            final Bill savedBill = repository.save(updatedBill);

            return savedBill.getId();
        }

        return StringUtils.EMPTY;

    }

    @Override
    public void remove(String billId) {
        if (StringUtils.isBlank(billId)) {
            log.error("BillId is empty");
            return;
        }
        repository.deleteById(billId);
    }

    @Override
    public List<Bill> getAll(){
        final Iterable<Bill> bills = repository.findAll();
        return IterableUtils.toList(bills);
    }

    @Override
    public boolean bulkSave(List<Bill> billList) throws IOException {
        return repository.bulkInsert(billList);
    }
}
