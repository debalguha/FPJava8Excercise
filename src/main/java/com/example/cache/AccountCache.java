package com.example.cache;

import com.example.domain.Account;
import com.example.domain.FxEntry;
import com.example.functions.Functions;
import cyclops.control.Try;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.functions.Functions.fileDataSuplier;
import static com.google.common.base.Strings.emptyToNull;
import static cyclops.control.Try.withCatch;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

public class AccountCache {

    private Map<Person, Account> cache;

    public AccountCache(File accountFile) {
        cache = withCatch(fileDataSuplier(accountFile), IOException.class)
                .onFail(e -> {throw new RuntimeException("Unable to read account file");})
                .map(lines -> createAccounts(lines))
                .orElse(Collections.emptyList())
                .stream().collect(toMap(Account::getPerson, Function.identity()));
    }

    private List<Account> createAccounts(List<String> lines) {
        return lines.stream().map(line -> fromLine(line))
                .flatMap(opt -> opt.map(v -> Stream.of(v)).orElse(Stream.empty()))
                /*.filter(opt -> opt.isPresent())
                .map(opt -> opt.get())*/
                .collect(Collectors.toList());
    }

    private Optional<Account> fromLine(String inputLine) {
        return ofNullable(emptyToNull(inputLine))
                .filter(s -> !s.startsWith("#"))
                .filter(s -> Functions.hasAllColumns(s, 6))
                //Chain more validations
                .map(s -> createAccount(s));
    }

    private Account createAccount(String s) {
        String[] split = s.split(",", -1);
        long accountId = Long.valueOf(split[0]);
        BigDecimal balance = new BigDecimal(split[1]);
        return new Account(accountId, balance, new Person(stream(split).skip(2).collect(joining())));
    }


}
