package com.kashyap.homeIdeas.billmonitor.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Map;

public class BillDto {

    private String billId;
    private String orgName;
    private String userId;
    private String serviceId;
    private String type;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date issueDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date dueDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date payDate;

    private PaymentDetailDto paymentDetail;
    private int totalAmount;
    private int totalAmountAfterExpiry;
    private Map<String, Object> extraInfo;

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
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

    public Map<String, Object> getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(Map<String, Object> extraInfo) {
        this.extraInfo = extraInfo;
    }

    public String getServiceId(){
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public int getTotalAmountAfterExpiry() {
        return totalAmountAfterExpiry;
    }

    public void setTotalAmountAfterExpiry(int totalAmountAfterExpiry) {
        this.totalAmountAfterExpiry = totalAmountAfterExpiry;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }
}
