package com.kashyap.homeIdeas.billmonitor.exception;

import com.kashyap.homeIdeas.billmonitor.dto.ApplicationResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

import static com.kashyap.homeIdeas.billmonitor.constant.ApplicationConstant.RESOURCE_NOT_FOUND;
import static com.kashyap.homeIdeas.billmonitor.constant.ApplicationConstant.SOMETHING_WENT_WRONG;

/**
 * @author Kashyap Kadiya
 * @since 2021-06
 */
@ControllerAdvice
@ResponseBody
public class ControllerExceptionHandler {

    private final static Logger log = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApplicationResponse handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.error("ERROR: "+ex);
        final Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) ->{
            final String fieldName = ((FieldError) error).getField();
            final String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        return new ApplicationResponse(false, StringUtils.join(errors), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(NoRecordFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApplicationResponse handleNoRecordFoundException(NoRecordFoundException ex) {
        log.error("ERROR: "+ex);
        final String message = ex.getMessage() == null
                ? RESOURCE_NOT_FOUND
                : ex.getMessage();

        return new ApplicationResponse(false,
                message,
                HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApplicationResponse handleDefaultException(Exception ex) {
        log.error("ERROR: "+ex);
        final String message = ex.getMessage() == null
                ? SOMETHING_WENT_WRONG
                : ex.getMessage();

        return new ApplicationResponse(false,
                message,
                HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}