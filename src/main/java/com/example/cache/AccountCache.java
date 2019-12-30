package com.example.cache;

import com.example.domain.Account;

import java.util.Date;
import java.util.Optional;

public interface AccountCache {
    Optional<Account> findByAccountId(long accountId);
    Optional<Account> findByLastNameAndDOB(String lastName, Date dob);
    Optional<Account> findByLastNameAndTFNAndDOB(String lastName, String tfn, Date dob);
}
