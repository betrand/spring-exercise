package com.risknarrative.springexercise.companysearch.exception.model;

public class CompanySearchException extends RuntimeException {
    private final int status;

    public CompanySearchException(String message, int status) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}

