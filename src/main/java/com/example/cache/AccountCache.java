package com.example.cache;

import com.example.domain.Account;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;

import static com.example.functions.Functions.*;
import static cyclops.control.Try.withCatch;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

public class AccountCache {

    private Map<Person, Account> cache;

    public AccountCache(File accountFile) {
        cache = withCatch(fileDataSupplier(accountFile), IOException.class)
                .onFail(e -> {throw new RuntimeException("Unable to read account file");})
                .map(lines -> createAccounts(lines))
                .orElse(Collections.emptyList())
                .stream().collect(toMap(Account::getPerson, Function.identity()));
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
