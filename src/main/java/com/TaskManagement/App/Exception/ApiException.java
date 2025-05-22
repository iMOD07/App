package com.TaskManagement.App.Exception;

public class ApiException extends RuntimeException {
    private final String messageAr;
    private final String messageEn;

    public ApiException(String messageAr, String messageEn) {
        super(messageEn); // for logs/debug
        this.messageAr = messageAr;
        this.messageEn = messageEn;

    }

    public String getMessageAr() {
        return messageAr;
    }

    public String getMessageEn() {
        return messageEn;
    }
}
