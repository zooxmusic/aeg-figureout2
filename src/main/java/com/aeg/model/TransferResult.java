package com.aeg.model;


public class TransferResult {
    private final static String SUCCESS = "Success";
    private final static String FAILED = "Fail";
    private String reason;
    private String message;

    public static TransferResult successful() {
        return new TransferResult(SUCCESS);
    }
    public static TransferResult failed(String reason) {
        return new TransferResult(FAILED);
    }

    private TransferResult(String message) {
        this.message = message;
    }
    private TransferResult(String message, String reason) {
        this.message = message;
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public String getMessage() {
        return message;
    }
}
