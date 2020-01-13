package com.example.cache;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.time.LocalDate;

public class AccountCacheTest {
    @Test
    public void shouldBeAbleToReadAndCacheAccounts() throws Exception {
        final AccountCache cache = new AccountCacheImpl(new File("src/test/resources/account-master.txt"));
        Assert.assertTrue(cache.findByAccountId(1).isPresent());
        LocalDate dob = LocalDate.of(2010, 11, 12);
        Assert.assertTrue(cache.findByLastNameAndDOB("Trump", dob).isPresent());
    }
}
