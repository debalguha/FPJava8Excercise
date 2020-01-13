package com.example.cache;

import com.example.domain.Account;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.example.functions.Functions.*;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;

public class AccountCacheImpl implements AccountCache{

    private Map<Person, Account> cache;

    public AccountCacheImpl(File accountFile) {
        cache = createFromFile(() -> readFileToLines(accountFile), this::createAccounts, a -> a.person, identity());
    }

    private List<Account> createAccounts(List<String> lines) {
        Predicate<String> validationPredicate = notNullOrEmptyLinesPredicate.and(unCommentedLinesPredicate).and(columnNumberPredicateFunc.apply(6));
        return createFromLines(lines, s -> createAccount(s), validationPredicate)
                .stream()
                .flatMap(opt -> opt.map(Stream:: of).orElse(Stream.empty()))
                .collect(toList());
    }

    @Override
    public Optional<Account> findByAccountId(long accountId) {
        return cache.values().stream().filter(a -> a.accountId == accountId).reduce((element, otherElement) -> {
            throw new DuplicateAccountException();
        });
    }

    @Override
    public Optional<Account> findByLastNameAndDOB(String lastName, LocalDate dob) {
        return cache.values().stream()
                .filter(accountPredicateByLastNameFunc.apply(lastName))
                .filter(accountPredicateByDobFunc.apply(dob))
                .findFirst();
    }

    @Override
    public Optional<Account> findByLastNameAndTFNAndDOB(String lastName, String tfn, LocalDate dob) {
        return cache.values().stream()
                .filter(accountPredicateByLastNameFunc.apply(lastName))
                .filter(accountPredicateByDobFunc.apply(dob))
                .filter(accountPredicateByTfnFunc.apply(tfn))
                .findFirst();
    }
}
