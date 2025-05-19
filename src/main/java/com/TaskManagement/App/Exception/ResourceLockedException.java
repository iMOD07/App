package com.TaskManagement.App.Exception;

public class ResourceLockedException extends RuntimeException {
    public ResourceLockedException(String message) {
        super(message);
    }
}
