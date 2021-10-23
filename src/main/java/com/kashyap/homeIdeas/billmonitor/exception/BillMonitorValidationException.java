package com.kashyap.homeIdeas.billmonitor.exception;

public class BillMonitorValidationException extends RuntimeException{

    private String message;

    public BillMonitorValidationException(){
        super();
    }

    public BillMonitorValidationException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
