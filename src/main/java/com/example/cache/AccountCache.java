package com.example.cache;

import com.example.domain.Account;

import java.time.LocalDate;
import java.util.Optional;

public interface AccountCache {
    Optional<Account> findByAccountId(long accountId);
    Optional<Account> findByLastNameAndDOB(String lastName, LocalDate dob);
    Optional<Account> findByLastNameAndTFNAndDOB(String lastName, String tfn, LocalDate dob);
}
