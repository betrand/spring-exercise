package com.risknarrative.springexercise.companysearch.exception.handler;

import static com.risknarrative.springexercise.companysearch.util.MessageUtil.INTERNAL_SERVER_ERROR_MSG;
import static com.risknarrative.springexercise.companysearch.util.MessageUtil.INTERNAL_SERVER_ERROR_CODE;
import com.risknarrative.springexercise.companysearch.exception.model.CompanySearchException;
import com.risknarrative.springexercise.companysearch.exception.model.ExceptionDetail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class CompanySearchExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CompanySearchException.class)
    public ResponseEntity<Object> handleCompanySearchException(CompanySearchException ex) {

        ExceptionDetail detail = new ExceptionDetail(
                ex.getMessage(),
                ex.getStatus(),
                LocalDateTime.now());

        return new ResponseEntity<>(detail, HttpStatus.valueOf(ex.getStatus()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {

        ExceptionDetail detail = new ExceptionDetail(
                INTERNAL_SERVER_ERROR_MSG,
                INTERNAL_SERVER_ERROR_CODE,
                LocalDateTime.now());

        return new ResponseEntity<>(detail, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
