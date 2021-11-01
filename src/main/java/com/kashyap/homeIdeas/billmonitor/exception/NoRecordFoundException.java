package com.kashyap.homeIdeas.billmonitor.exception;

/**
 * @author Kashyap Kadiya
 * @since 2021-06
 */
public class NoRecordFoundException extends RuntimeException{

    private String message;
    private Integer code;

    public NoRecordFoundException() {
        super();
    }

    public NoRecordFoundException(String message) {
        super(message);
        this.message = message;
    }

    public NoRecordFoundException(String message, Integer code) {
        super(message);
        this.message = message;
        this.code = code;
    }

    @Override
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
}
