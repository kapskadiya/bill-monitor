package com.kashyap.homeIdeas.billmonitor.builder;

import com.kashyap.homeIdeas.billmonitor.model.Attachment;
import com.kashyap.homeIdeas.billmonitor.model.Bill;
import com.kashyap.homeIdeas.billmonitor.model.BillType;
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

    public BillBuilder setId(String id) {
        if (StringUtils.isNotBlank(id)) {
            this.bill.setId(id);
        }
        return this;
    }

    public BillBuilder setOrgName(String orgName) {
        if (StringUtils.isNotBlank(orgName)) {
            this.bill.setOrgName(orgName);
        }
        return this;
    }

    public BillBuilder setCustomerName(String customerName) {
        if (StringUtils.isNotBlank(customerName)) {
            this.bill.setCustomerName(customerName);
        }
        return this;
    }

    public BillBuilder setType(BillType type) {
        if (type != null) {
            this.bill.setType(type);
        }
        return this;
    }

    public BillBuilder setAmountToBePay(int amountToBePay) {
        this.bill.setAmountToBePay(amountToBePay);
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

    public BillBuilder setPaymentDetail(PaymentDetail paymentDetail) {
        if (paymentDetail != null) {
            this.bill.setPaymentDetail(paymentDetail);
        }
        return this;
    }

    public BillBuilder setBillingDurationInDays(int billingDurationInDays) {
        this.bill.setBillingDurationInDays(billingDurationInDays);
        return this;
    }

    public BillBuilder setMetadata(Map<String, Object> metadata) {
        if (MapUtils.isNotEmpty(metadata)) {
            this.bill.setMetadata(metadata);
        }
        return this;
    }

    public Bill build() {
        return this.bill;
    }

}
