package com.example.cache;

import com.example.domain.Account;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.functions.Functions.*;
import static java.util.function.Function.identity;

public class AccountCacheImpl implements AccountCache{

    private Map<Person, Account> cache;

    public AccountCacheImpl(File accountFile) {
        cache = createFromFile(accountFile, this::createAccounts, a -> a.person, identity());
    }

    private List<Account> createAccounts(List<String> lines) {
        return createFromLines(lines, s -> createAccount(s));
    }

    @Override
    public Optional<Account> findByAccountId(long accountId) {
        return cache.values().stream().filter(a -> a.accountId == accountId).reduce((element, otherElement) -> {
            throw new DuplicateAccountException();
        });
    }

    @Override
    public Optional<Account> findByLastNameAndDOB(String lastName, Date dob) {
        return null;
    }

    @Override
    public Optional<Account> findByLastNameAndTFNAndDOB(String lastName, String tfn, Date dob) {
        return null;
    }
}
