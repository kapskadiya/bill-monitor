package com.kashyap.homeIdeas.billmonitor.model;

import java.io.Serializable;

public class PaymentDetail implements Serializable {

    private String id;
    private String method;
    private String platform;
    private String payBy;
    private PaymentStatus status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPayBy() {
        return payBy;
    }

    public void setPayBy(String payBy) {
        this.payBy = payBy;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
}
