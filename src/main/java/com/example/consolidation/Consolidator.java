package com.example.consolidation;

import com.example.cache.AccountCache;
import com.example.cache.AccountCacheImpl;
import com.example.cache.FXCache;

import java.io.File;

public class Consolidator {
    public final File transactionFile;
    public final File fxCacheFile;
    public final File accountFile;

    public Consolidator(File transactionFile, File fxCacheFile, File accountFile) {
        this.transactionFile = transactionFile;
        this.fxCacheFile = fxCacheFile;
        this.accountFile = accountFile;
    }

    public void process() {
        FXCache fxCache = new FXCache(fxCacheFile);
        AccountCache accountCache = new AccountCacheImpl(accountFile);


    }

}
