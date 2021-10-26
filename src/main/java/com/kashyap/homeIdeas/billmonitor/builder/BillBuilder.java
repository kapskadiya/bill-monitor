package com.kashyap.homeIdeas.billmonitor.builder;

import com.kashyap.homeIdeas.billmonitor.model.Bill;
import com.kashyap.homeIdeas.billmonitor.constant.BillType;
import com.kashyap.homeIdeas.billmonitor.model.PaymentDetail;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Map;

public class BillBuilder {

    private final Bill bill;

    public BillBuilder() {
        bill = new Bill();
    }


    public BillBuilder setBillId(String id) {
        if (StringUtils.isNotBlank(id)) {
            this.bill.setBillId(id);
        }
        return this;
    }

    public BillBuilder setOrgName(String orgName) {
        if (StringUtils.isNotBlank(orgName)) {
            this.bill.setOrgName(orgName);
        }
        return this;
    }

    public BillBuilder setType(String type) {
        if (StringUtils.isNotBlank(type)) {
            this.bill.setType(BillType.getBillType(type));
        }
        return this;
    }

    public BillBuilder setTotalAmount(int totalAmount) {
        this.bill.setTotalAmount(totalAmount);
        return this;
    }

    public BillBuilder setIssueDate(Date issueDate) {
        if (issueDate != null) {
            this.bill.setIssueDate(issueDate);
        }
        return this;
    }

    public BillBuilder setDueDate(Date dueDate) {
        if (dueDate != null) {
            this.bill.setDueDate(dueDate);
        }
        return this;
    }

    public BillBuilder setPayDate(Date payDate) {
        if (payDate != null) {
            this.bill.setPayDate(payDate);
        }
        return this;
    }

    public BillBuilder setPaymentDetail(PaymentDetail paymentDetail) {
        if (paymentDetail != null) {
            this.bill.setPaymentDetail(paymentDetail);
        }
        return this;
    }

    public BillBuilder setTotalAmountAfterExpiry(int totalAmountAfterExpiry) {
        this.bill.setTotalAmountAfterExpiry(totalAmountAfterExpiry);
        return this;
    }

    public BillBuilder setExtraInfo(Map<String, Object> info) {
        if (MapUtils.isNotEmpty(info)) {
            this.bill.setExtraInfo(info);
        }
        return this;
    }

    public BillBuilder setCustomerId(String customerId) {
        if (StringUtils.isNotBlank(customerId)) {
            this.bill.setCustomerId(customerId);
        }
        return this;
    }

    public Bill build() {
        return this.bill;
    }

}
