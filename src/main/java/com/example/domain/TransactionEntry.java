package com.example.domain;

import com.example.cache.Person;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

public class TransactionEntry {
    public final long transactionId;
    public final long accountId;
    public final BigDecimal transactionAmount;
    public final TransactionType transactionType;
    public final Currency currency;
    public final Person person;

    public TransactionEntry(long transactionId, long accountId, BigDecimal transactionAmount, TransactionType transactionType, Currency currency, Person person) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.transactionAmount = transactionAmount;
        this.transactionType = transactionType;
        this.currency = currency;
        this.person = person;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionEntry that = (TransactionEntry) o;
        return Objects.equals(transactionId, that.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId);
    }

    @Override
    public String toString() {
        return "TransactionEntry{" +
                "transactionId=" + transactionId +
                ", accountId=" + accountId +
                ", transactionAmount=" + transactionAmount +
                ", transactionType=" + transactionType +
                ", currency=" + currency +
                ", person=" + person +
                '}';
    }
}
