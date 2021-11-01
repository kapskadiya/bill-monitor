package com.kashyap.homeIdeas.billmonitor.model;

import com.kashyap.homeIdeas.billmonitor.constant.BillType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * @author Kashyap Kadiya
 * @since 2021-06
 */
@Document(indexName = "bill")
public class Bill implements Serializable {

    @Id
    @ReadOnlyProperty
    private String id;

    @Field(type = FieldType.Keyword)
    private String billId;

    @Field(type = FieldType.Text)
    private String orgName;

    @Field(type = FieldType.Text)
    private String customerId;

    @Field(type = FieldType.Keyword)
    private BillType type;

    @Field(type = FieldType.Date, format = {DateFormat.epoch_millis})
    private Date issueDate;

    @Field(type = FieldType.Date, format = {DateFormat.epoch_millis})
    private Date dueDate;

    @Field(type = FieldType.Date, format = {DateFormat.epoch_millis})
    private Date payDate;

    @Field(type = FieldType.Nested)
    private PaymentDetail paymentDetail;

    @Field(type = FieldType.Integer)
    private int totalAmount;

    @Field(type = FieldType.Integer)
    private int totalAmountAfterExpiry;

    @Field(type = FieldType.Nested)
    private Map<String, Object> extraInfo;

    @Field(type = FieldType.Text)
    private String createdBy;

    @Field(type = FieldType.Text)
    private String updatedBy;

    @Field(type = FieldType.Date, format = {DateFormat.epoch_millis})
    private Date createdDate;

    @Field(type = FieldType.Date, format = {DateFormat.epoch_millis})
    private Date updatedDate;

    @Field(type = FieldType.Boolean)
    private boolean isDeleted;

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

    public BillType getType() {
        return type;
    }

    public void setType(BillType type) {
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

    public PaymentDetail getPaymentDetail() {
        return paymentDetail;
    }

    public void setPaymentDetail(PaymentDetail paymentDetail) {
        this.paymentDetail = paymentDetail;
    }

    public Map<String, Object> getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(Map<String, Object> extraInfo) {
        this.extraInfo = extraInfo;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public int getTotalAmountAfterExpiry() {
        return totalAmountAfterExpiry;
    }

    public void setTotalAmountAfterExpiry(int totalAmountAfterExpiry) {
        this.totalAmountAfterExpiry = totalAmountAfterExpiry;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }
}
