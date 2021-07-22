package com.kashyap.homeIdeas.billmonitor.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Document(indexName = "bill")
public class Bill implements Serializable {

    @Id
    @ReadOnlyProperty
    private String id;

    @Field(type = FieldType.Text)
    private String orgName;

    @Field(type = FieldType.Text)
    private String customerName;

    @Field(type = FieldType.Text)
    private String customerId;

    @Field(type = FieldType.Text)
    private BillType type;

    @Field(type = FieldType.Integer)
    private int amountToBePay;

    @Field(type = FieldType.Date)
    private Date issueDate;

    @Field(type = FieldType.Date)
    private Date dueDate;

    @Field(type = FieldType.Nested)
    private PaymentDetail paymentDetail;

    @Field(type = FieldType.Integer)
    private int billingDurationInDays;

    @Field(type = FieldType.Nested)
    private Map<String, Object> metadata;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public BillType getType() {
        return type;
    }

    public void setType(BillType type) {
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

    public PaymentDetail getPaymentDetail() {
        return paymentDetail;
    }

    public void setPaymentDetail(PaymentDetail paymentDetail) {
        this.paymentDetail = paymentDetail;
    }

    public int getBillingDurationInDays() {
        return billingDurationInDays;
    }

    public void setBillingDurationInDays(int billingDurationInDays) {
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
