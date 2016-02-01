package com.eutechpro.allytransitapp.data;

/**
 * Error class that carry information about API errors.
 */
@SuppressWarnings("unused")
public class DataError {
    /**
     * API Error code.
     */
    private int    code;
    /**
     * API Error message. <br/>
     * <i>For example, in case of wrong lat/lng pair sent, appropriate error code and message should be returned</i>
     */
    private String errorMessage;

    public DataError(int code, String errorMessage) {
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
