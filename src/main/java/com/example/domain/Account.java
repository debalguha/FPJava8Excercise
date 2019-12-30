package com.example.domain;

import com.example.cache.Person;

import java.math.BigDecimal;
import java.util.Objects;

public class Account {
    public final long accountId;
    public final BigDecimal balance;
    public final Person person;


    public Account(long accountId, BigDecimal balance, Person person) {
        this.accountId = accountId;
        this.balance = balance;
        this.person = person;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(accountId, account.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId);
    }

    public long getAccountId() {
        return accountId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Person getPerson() {
        return person;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", balance=" + balance +
                ", person=" + person +
                '}';
    }
}
