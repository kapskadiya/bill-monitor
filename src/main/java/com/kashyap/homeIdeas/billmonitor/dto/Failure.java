package com.kashyap.homeIdeas.billmonitor.dto;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Failure {
    private String reason = StringUtils.EMPTY;
    private String exception = StringUtils.EMPTY;
    private List<String> fields = new ArrayList<>();

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }
}
