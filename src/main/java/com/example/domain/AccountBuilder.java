package com.example.domain;

import com.example.cache.Person;

import java.math.BigDecimal;

public final class AccountBuilder {
    public long accountId;
    public BigDecimal balance;
    public Person person;

    private AccountBuilder() {
    }

    public static AccountBuilder anAccount() {
        return new AccountBuilder();
    }

    public AccountBuilder accountId(long accountId) {
        this.accountId = accountId;
        return this;
    }

    public AccountBuilder balance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }

    public AccountBuilder person(Person person) {
        this.person = person;
        return this;
    }

    public static AccountBuilder fromAccount(Account anAccount) {
        return anAccount().accountId(anAccount.accountId).balance(anAccount.balance).person(anAccount.person);
    }

    public Account build() {
        return new Account(accountId, balance, person);
    }
}
