package com.example.consolidation;

public class InsufficientDataException extends RuntimeException {
    public final String reasonString;
    public InsufficientDataException(String reasonString) {
        this.reasonString = reasonString;
    }
}
