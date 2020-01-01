package com.example.domain;

import com.example.cache.Person;

import java.math.BigDecimal;
import java.util.Currency;

public final class TransactionEntryBuilder {
    public long transactionId;
    public long accountId;
    public BigDecimal transactionAmount;
    public TransactionType transactionType;
    public Currency currency;
    public Person person;

    private TransactionEntryBuilder() {
    }

    public static TransactionEntryBuilder aTransactionEntry() {
        return new TransactionEntryBuilder();
    }

    public TransactionEntryBuilder withTransactionId(long transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public TransactionEntryBuilder withAccountId(long accountId) {
        this.accountId = accountId;
        return this;
    }

    public TransactionEntryBuilder withTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
        return this;
    }

    public TransactionEntryBuilder withTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
        return this;
    }

    public TransactionEntryBuilder withCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public TransactionEntryBuilder withPerson(Person person) {
        this.person = person;
        return this;
    }

    public TransactionEntry build() {
        return new TransactionEntry(transactionId, accountId, transactionAmount, transactionType, currency, person);
    }
}
