package com.example.monad.state;

public class CurrencyMessage implements Message{
    public final String currency;

    public CurrencyMessage(String currency) {
        this.currency = currency;
    }

    @Override
    public MessageType messageType() {
        return MessageType.CURRENCY;
    }
}
