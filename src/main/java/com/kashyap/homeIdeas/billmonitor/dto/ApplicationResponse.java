package com.kashyap.homeIdeas.billmonitor.dto;

import java.util.HashMap;
import java.util.Map;

public class ApplicationResponse {

    private Map<String, Object> success = new HashMap<>();
    private Failure failure = new Failure();

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

    public void setSuccess(String field, Object value) {
        this.success.put(field, value);
    }
}
