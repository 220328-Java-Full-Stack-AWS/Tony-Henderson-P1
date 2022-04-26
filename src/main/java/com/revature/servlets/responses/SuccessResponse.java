package com.revature.servlets.responses;

public class SuccessResponse {

    private int statusCode;
    private String message;
    private Object data;

    public SuccessResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public SuccessResponse(int statusCode, String message, Object objectData) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = objectData;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
