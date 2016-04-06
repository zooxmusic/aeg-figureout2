package com.aeg.model;


public class TransferResult {
    private final static String SUCCESS = "Success";
    private final static String FAILED = "Faile";
    private String message;

    public static TransferResult successful() {
        return new TransferResult(SUCCESS);
    }
    public static TransferResult failed() {
        return new TransferResult(FAILED);
    }

    private TransferResult(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
