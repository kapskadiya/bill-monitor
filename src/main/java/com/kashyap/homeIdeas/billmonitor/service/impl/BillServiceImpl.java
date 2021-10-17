package com.kashyap.homeIdeas.billmonitor.service.impl;

import com.kashyap.homeIdeas.billmonitor.model.Bill;
import com.kashyap.homeIdeas.billmonitor.model.BillType;
import com.kashyap.homeIdeas.billmonitor.model.PaymentDetail;
import com.kashyap.homeIdeas.billmonitor.model.User;
import com.kashyap.homeIdeas.billmonitor.repostiory.BillRepository;
import com.kashyap.homeIdeas.billmonitor.service.AuthenticationService;
import com.kashyap.homeIdeas.billmonitor.service.BillService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BillServiceImpl implements BillService {

    private static final Logger log = LoggerFactory.getLogger(BillServiceImpl.class);

    @Autowired
    private BillRepository repository;

    @Autowired
    private AuthenticationService authService;

    @Override
    public void save(Bill bill) {
        if (bill == null) {
            throw new IllegalArgumentException("bill object is null.");
        }

        final User loggedInUser = authService.getLoggedInUser();
        final String createdBy = loggedInUser != null ? loggedInUser.getId() : "admin@admin.com";

        bill.setCreatedBy(createdBy);
        bill.setCreatedDate(new Date());
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
    public Bill getByBillId(String billId) {
        if (StringUtils.isBlank(billId)) {
            log.error("BillId is empty");
            return new Bill();
        }
        final List<Bill> billList = repository.findByBillId(billId);
        if (CollectionUtils.isEmpty(billList)) {
            throw new IllegalArgumentException("Bill is not present in the system.");
        }
        return billList.get(0);
    }

    @Override
    public List<Bill> getByCustomerId(String customerId) {
        if (StringUtils.isBlank(customerId)) {
            log.error("CustomerName is empty");
            return new ArrayList<>();
        }
        return repository.findByCustomerId(customerId);
    }

    @Override
    public void update(Bill newBill) {
        if (newBill == null) {
            throw new IllegalArgumentException("Bill object is null.");
        }

        final Bill updatedBill = getByBillId(newBill.getBillId());

        if (StringUtils.isNotBlank(newBill.getOrgName())) {
            updatedBill.setOrgName(newBill.getOrgName());
        }
        if (StringUtils.isNotBlank(newBill.getCustomerId())) {
            updatedBill.setCustomerId(newBill.getCustomerId());
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

        final User loggedInUser = authService.getLoggedInUser();
        final String updatedBy = loggedInUser != null ? loggedInUser.getId() : "admin@admin.com";

        updatedBill.setUpdatedBy(updatedBy);
        updatedBill.setUpdatedDate(new Date());

        repository.save(updatedBill);
    }

    @Override
    public void deleteById(String billId) throws IOException {
        if (StringUtils.isBlank(billId)) {
            log.error("BillId is empty");
            return;
        }
        final String esId = repository.getOnlyESIdByBillId(billId);

        if (StringUtils.isNotBlank(esId)) {
            repository.updateIsDeleted(esId, true);
        }
    }

    @Override
    public void deleteByCustomerId(String customerId) throws IOException {
        if (StringUtils.isBlank(customerId)) {
            log.error("customerId is empty");
            return;
        }
        final List<String> esIdList = repository.getOnlyESIdsByCustomerId(customerId);
        if (CollectionUtils.isEmpty(esIdList)) {
            return;
        }
        repository.bulkPartialUpdate(esIdList, "isDeleted", "false");
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

    @Override
    public List<Bill> getByIsDeleted(boolean isDeleted) {
        return repository.findByIsDeleted(isDeleted);
    }
}
