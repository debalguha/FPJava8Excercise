package com.example.consolidation;

public class InvalidTransactionException extends RuntimeException {
    public final String reasonString;
    public InvalidTransactionException(String reason) {
        this.reasonString = reason;
    }
}
