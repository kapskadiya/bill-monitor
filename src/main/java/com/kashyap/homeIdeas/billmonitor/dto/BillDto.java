package com.kashyap.homeIdeas.billmonitor.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Map;

public class BillDto {

    private String orgName;
    private String customerName;
    private String customerId;
    private String type;
    private int amountToBePay;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date issueDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date dueDate;

    private PaymentDetailDto paymentDetail;
    private Integer billingDurationInDays;
    private Map<String, Object> metadata;

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAmountToBePay() {
        return amountToBePay;
    }

    public void setAmountToBePay(int amountToBePay) {
        this.amountToBePay = amountToBePay;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public PaymentDetailDto getPaymentDetail() {
        return paymentDetail;
    }

    public void setPaymentDetail(PaymentDetailDto paymentDetail) {
        this.paymentDetail = paymentDetail;
    }

    public Integer getBillingDurationInDays() {
        return billingDurationInDays;
    }

    public void setBillingDurationInDays(Integer billingDurationInDays) {
        this.billingDurationInDays = billingDurationInDays;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
