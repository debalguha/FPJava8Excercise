package com.example.monad.state;

public class TransactionMessage implements Message {
    public final String stock;
    public final double amount;
    public final long quantity;
    public final TransactionType transactionType;

    public TransactionMessage(String stock, double amount, long quantity, TransactionType transactionType) {
        this.stock = stock;
        this.amount = amount;
        this.quantity = quantity;
        this.transactionType = transactionType;
    }

    @Override
    public MessageType messageType() {
        return MessageType.TRANSACTION;
    }
}
