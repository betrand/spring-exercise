package com.risknarrative.springexercise.companysearch.util;

public final class MessageUtil {

    public static final int NOT_FOUND_ERROR_CODE = 404;
    public static final int BAD_REQUEST_ERROR_CODE = 400;
    public static final int INTERNAL_SERVER_ERROR_CODE = 500;

    public static final String DEFAULT_STATUS = "none";
    public static final String X_API_KEY = "x-api-key";

    public static final String X_API_KEY_REQUIRED_MSG = "x-api-key is required in the request header";
    public static final String COMPANY_NOT_FOUND_MSG = "Company not found";
    public static final String INTERNAL_SERVER_ERROR_MSG = "An unexpected internal server error occurred";
    public static final String COMPANY_NUM_OR_NAME_REQUIRED_MSG = "Request input error: company Number or Name is required";

}
