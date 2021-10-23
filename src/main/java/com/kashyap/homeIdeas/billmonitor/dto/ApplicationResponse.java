package com.kashyap.homeIdeas.billmonitor.dto;

public class ApplicationResponse {

    private Boolean success;
    private String message;
    private Integer code;
    private Object data;

    public ApplicationResponse() {
    }

    public ApplicationResponse(Boolean success, String message, Integer code) {
        this.success = success;
        this.message = message;
        this.code = code;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
