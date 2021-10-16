package com.kashyap.homeIdeas.billmonitor.model;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

public class PaymentDetail implements Serializable {

    @Field(type = FieldType.Keyword)
    private String transactionId;

    @Field(type = FieldType.Keyword)
    private PaymentType type;

    @Field(type = FieldType.Keyword)
    private PaymentMethodType method;

    @Field(type = FieldType.Keyword)
    private String methodNumber;

    @Field(type = FieldType.Keyword)
    private PaymentStatus status;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public PaymentMethodType getMethod() {
        return method;
    }

    public void setMethod(PaymentMethodType method) {
        this.method = method;
    }

    public String getMethodNumber() {
        return methodNumber;
    }

    public void setMethodNumber(String methodNumber) {
        this.methodNumber = methodNumber;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public PaymentType getType() {
        return type;
    }

    public void setType(PaymentType type) {
        this.type = type;
    }
}
