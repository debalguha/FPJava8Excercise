package com.example.domain;

import com.example.cache.Person;

import java.math.BigDecimal;
import java.util.Objects;

public class TransactionEntry {
    public final Long transactionId;
    public final Long accountId;
    public final BigDecimal transactionAmount;
    public final TransactionType transactionType;
    public final Person person;

    public TransactionEntry(Long transactionId, Long accountId, BigDecimal transactionAmount, TransactionType transactionType, Person person) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.transactionAmount = transactionAmount;
        this.transactionType = transactionType;
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
                ", person=" + person +
                '}';
    }
}
