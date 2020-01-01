package com.example.operations;

import com.example.domain.Account;

import java.math.BigDecimal;
import java.util.function.BiFunction;

public class AccountOperation {
    public static BiFunction<Account, BigDecimal, Account> debit = (acc, amount) -> new Account(acc.accountId, acc.balance.subtract(amount), acc.person);
    public static BiFunction<Account, BigDecimal, Account> credit = (acc, amount) -> new Account(acc.accountId, acc.balance.add(amount), acc.person);
}
