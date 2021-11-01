package com.kashyap.homeIdeas.billmonitor.exception;

/**
 * @author Kashyap Kadiya
 * @since 2021-06
 */
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
