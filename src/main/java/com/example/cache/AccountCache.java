package com.example.cache;

import com.example.domain.Account;

import java.io.File;
import java.util.List;
import java.util.Map;

import static com.example.functions.Functions.*;
import static java.util.function.Function.identity;

public class AccountCache {

    private Map<Person, Account> cache;

    public AccountCache(File accountFile) {
        cache = createFromFile(accountFile, this::createAccounts, a -> a.person, identity());
    }

    private List<Account> createAccounts(List<String> lines) {
        return createFromLines(lines, s -> createAccount(s));
    }

}
