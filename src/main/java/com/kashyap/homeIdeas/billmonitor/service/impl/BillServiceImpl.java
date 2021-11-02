package com.kashyap.homeIdeas.billmonitor.service.impl;

import com.kashyap.homeIdeas.billmonitor.constant.BillType;
import com.kashyap.homeIdeas.billmonitor.exception.BillMonitorValidationException;
import com.kashyap.homeIdeas.billmonitor.exception.NoRecordFoundException;
import com.kashyap.homeIdeas.billmonitor.model.Bill;
import com.kashyap.homeIdeas.billmonitor.model.PaymentDetail;
import com.kashyap.homeIdeas.billmonitor.model.User;
import com.kashyap.homeIdeas.billmonitor.repostiory.BillRepository;
import com.kashyap.homeIdeas.billmonitor.service.AuthenticationService;
import com.kashyap.homeIdeas.billmonitor.service.BillService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.kashyap.homeIdeas.billmonitor.constant.ApplicationConstant.DATA_INVALID;
import static com.kashyap.homeIdeas.billmonitor.constant.ApplicationConstant.IS_DELETED;

/**
 * @author Kashyap Kadiya
 * @since 2021-06
 */
@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private BillRepository repository;

    @Autowired
    private AuthenticationService authService;

    @Override
    public void save(Bill bill) {

        final User loggedInUser = authService.getLoggedInUser();

        bill.setCreatedBy(loggedInUser.getId());
        bill.setCreatedDate(new Date());
        repository.save(bill);
    }

    @Override
    public boolean isExist(String billId) {
        return repository.existsById(billId);
    }

    @Override
    public Bill getById(String billId) {
        final Optional<Bill> bill = repository.findById(billId);
        return bill.orElseThrow(NoRecordFoundException::new);
    }

    @Override
    public Bill getByBillId(String billId) {

        final List<Bill> billList = repository.findByBillId(billId);
        if (CollectionUtils.isEmpty(billList)) {
            throw new NoRecordFoundException();
        }
        return billList.get(0);
    }

    @Override
    public List<Bill> getByCustomerId(String customerId) {
        return repository.findByCustomerId(customerId);
    }

    @Override
    public void update(Bill newBill) {
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

        updatedBill.setUpdatedBy(loggedInUser.getId());
        updatedBill.setUpdatedDate(new Date());

        repository.save(updatedBill);
    }

    @Override
    public void deleteById(String billId) throws IOException {
        if (StringUtils.isBlank(billId)) {
            throw new BillMonitorValidationException(DATA_INVALID+ " Data:"+billId);
        }
        final String esId = repository.findOnlyESIdByBillId(billId);

        if (StringUtils.isNotBlank(esId)) {
            repository.updateIsDeleted(esId, true);
        }
    }

    @Override
    public void deleteByCustomerId(String customerId) throws IOException {
        if (StringUtils.isBlank(customerId)) {
            throw new BillMonitorValidationException(DATA_INVALID+ " Data:"+customerId);
        }
        final List<String> esIdList = repository.findOnlyESIdsByCustomerId(customerId);
        if (CollectionUtils.isEmpty(esIdList)) {
            return;
        }
        repository.bulkPartialUpdate(esIdList, IS_DELETED, "false");
    }

    @Override
    public List<Bill> getAll(){
        final Iterable<Bill> bills = repository.findAll();
        return IterableUtils.toList(bills);
    }

    @Override
    public boolean bulkSave(List<Bill> billList) throws IOException {
        final User loggedInUser = authService.getLoggedInUser();

        billList.forEach(bill -> {
            bill.setCreatedBy(loggedInUser.getId());
            bill.setCreatedDate(new Date());
        });

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
