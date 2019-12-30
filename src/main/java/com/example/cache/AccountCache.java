package com.example.cache;

import com.example.domain.Account;
import com.example.functions.Functions;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.example.functions.Functions.createFromLines;
import static java.util.Arrays.stream;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.joining;

public class AccountCache {

    private Map<Person, Account> cache;

    public AccountCache(File accountFile) {
        cache = Functions.createFromFile(accountFile, this::createAccounts, a -> a.person, identity());
    }

    private List<Account> createAccounts(List<String> lines) {
        return createFromLines(lines, s -> createAccount(s));
    }

    private Account createAccount(String s) {
        String[] split = s.split(",", -1);
        long accountId = Long.valueOf(split[0]);
        BigDecimal balance = new BigDecimal(split[1]);
        return new Account(accountId, balance, new Person(stream(split).skip(2).collect(joining())));
    }


}
