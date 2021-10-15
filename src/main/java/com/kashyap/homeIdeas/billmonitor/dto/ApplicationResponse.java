package com.kashyap.homeIdeas.billmonitor.dto;

import java.util.HashMap;
import java.util.Map;

public class ApplicationResponse {

    private Map<String, Object> success;
    private Failure failure;
    private int httpCode;

    public Map<String, Object> getSuccess() {
        return success;
    }

    public void setSuccess(Map<String, Object> success) {
        this.success = success;
    }

    public Failure getFailure() {
        return failure;
    }

    public void setFailure(Failure failure) {
        this.failure = failure;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }
}
