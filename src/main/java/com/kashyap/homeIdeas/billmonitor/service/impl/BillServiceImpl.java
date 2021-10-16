package com.kashyap.homeIdeas.billmonitor.service.impl;

import com.kashyap.homeIdeas.billmonitor.model.Bill;
import com.kashyap.homeIdeas.billmonitor.model.BillType;
import com.kashyap.homeIdeas.billmonitor.model.PaymentDetail;
import com.kashyap.homeIdeas.billmonitor.repostiory.BillRepository;
import com.kashyap.homeIdeas.billmonitor.service.BillService;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class BillServiceImpl implements BillService {

    private static final Logger log = LoggerFactory.getLogger(BillServiceImpl.class);

    @Autowired
    private BillRepository repository;

    @Override
    public void save(Bill bill) {
        if (bill == null) {
            throw new IllegalArgumentException("bill object is null.");
        }
        repository.save(bill);
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
            throw new IllegalArgumentException("Bill object is null.");
        }

        final Optional<Bill> existingBillOptional = repository.findById(newBill.getId());

        if (existingBillOptional.isPresent()) {
            final Bill updatedBill = existingBillOptional.get();

            if (StringUtils.isNotBlank(newBill.getOrgName())) {
                updatedBill.setOrgName(newBill.getOrgName());
            }
            if (StringUtils.isNotBlank(newBill.getUserId())) {
                updatedBill.setUserId(newBill.getUserId());
            }
            if (StringUtils.isNotBlank(newBill.getServiceId())) {
                updatedBill.setServiceId(newBill.getServiceId());
            }
            if (newBill.getType() != null) {
                updatedBill.setType(newBill.getType());
            }
            if (newBill.getTotalAmount() != 0) {
                updatedBill.setTotalAmount(newBill.getTotalAmount());
            }
            if (newBill.getTotalAmountAfterExpiry() != 0) {
                updatedBill.setTotalAmountAfterExpiry(newBill.getTotalAmountAfterExpiry());
            }
            if (newBill.getIssueDate() != null) {
                updatedBill.setIssueDate(newBill.getIssueDate());
            }
            if (newBill.getDueDate() != null) {
                updatedBill.setDueDate(newBill.getDueDate());
            }
            if (newBill.getPayDate() != null) {
                updatedBill.setPayDate(newBill.getPayDate());
            }

            if (newBill.getPaymentDetail() != null) {
                final PaymentDetail newPaymentDetail = newBill.getPaymentDetail();

                if (StringUtils.isNotBlank(newPaymentDetail.getTransactionId())) {
                    updatedBill.getPaymentDetail().setTransactionId(newPaymentDetail.getTransactionId());
                }
                if (newPaymentDetail.getType() != null) {
                    updatedBill.getPaymentDetail().setType(newPaymentDetail.getType());
                }
                if (newPaymentDetail.getMethod() != null) {
                    updatedBill.getPaymentDetail().setMethod(newPaymentDetail.getMethod());
                }
                if (StringUtils.isNotBlank(newPaymentDetail.getMethodNumber())) {
                    updatedBill.getPaymentDetail().setMethodNumber(newPaymentDetail.getMethodNumber());
                }
                if (newPaymentDetail.getStatus() != null) {
                    updatedBill.getPaymentDetail().setStatus(newPaymentDetail.getStatus());
                }
            }

            if (MapUtils.isNotEmpty(newBill.getExtraInfo())) {
                updatedBill.getExtraInfo().putAll(newBill.getExtraInfo());
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

    @Override
    public List<Bill> getByType(BillType type) {
        return repository.findByType(type);
    }
}
