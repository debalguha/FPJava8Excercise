package com.example.consolidation;

import com.example.cache.AccountCache;
import com.example.cache.AccountCacheImpl;
import com.example.cache.FXCache;
import com.example.constructs.Either;
import com.example.constructs.Try;
import com.example.domain.Account;
import com.example.domain.TransactionEntry;
import com.example.domain.TransactionEntryBuilder;

import java.io.File;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.example.functions.Functions.*;
import static com.example.operations.AccountOperation.credit;
import static com.example.operations.AccountOperation.debit;

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

    public List<TransactionEntry> createTransactionEntries(List<String> lines, FXCache fxCache, AccountCache accountCache) {
        lines.stream()
                .filter(linesToDiscardPredicate())
                .map(line -> calculateValidationStatus(line))
                .map(eOrv -> dealWithALine(eOrv, fxCache, accountCache))
                .collect(Collectors.toList());
                //.collect(Collectors.groupingBy(Either:: isLeft, Collectors.groupingBy(eithr1 -> either1.i)));

        //return createFromLines(lines, s -> createTransactionEntry(s), validationPredicate);
        return null;
    }

    private Predicate<String> linesToDiscardPredicate() {
        return nullOrEmptyLinesPredicate
                .and(commentedLinesPredicate);
    }

    private Either<RuntimeException, String> areAllColumnsPresent(String line, int numColumns) {
        if(columnNumberPredicateFunc.apply(numColumns).test(line)) {
            return Either.right(line);
        } else {
            throw new InsufficientDataException(line);
        }
    }

    private Either<RuntimeException, String> areMandatoryColumnsPopulated(String line, int []columns) {
        if(mandatoryColumnsPredicateFunc.apply(columns).test(line)) {
            return Either.right(line);
        } else {
            throw new InsufficientDataException(line);
        }
    }


    /**
     * should chain all processing , lookups, calculations etc
     * @param errorOrValue
     * @param fxCache
     * @param accountCache
     * @return
     */
    private Either<RuntimeException, Account> dealWithALine(Either<RuntimeException, String> errorOrValue, FXCache fxCache, AccountCache accountCache) {

        return errorOrValue.flatMap(line ->
                Try.doTry(() -> createTransactionEntry(line))
                        .map(tran -> lookupAccount(tran, accountCache))
                        .map(tuplePair -> applyFxRate(tuplePair, fxCache))
                        .map(tuplePair -> applyTransactionOperation(tuplePair.a, tuplePair.b))
                        .leftMap(e -> new InvalidTransactionException(buildFailureString(e, line)))
        );
    }

    private String buildFailureString(RuntimeException e, String transactionEntry) {
        return String.format("%s %s %s", TransactionEntryStatus.INVALID, e.getMessage(), transactionEntry);
    }

    private Account applyTransactionOperation(Account account, TransactionEntry transactionEntry) {
        switch(transactionEntry.transactionType){
            case DEBIT: return debit.apply(account, transactionEntry.transactionAmount);
            case CREDIT: return credit.apply(account, transactionEntry.transactionAmount);
            default: return account;
        }
    }

    private Tuple2<Account, TransactionEntry> applyFxRate(Tuple2<Account, TransactionEntry> tuplePair, FXCache fxCache) throws FXConversionException {
        TransactionEntry transactionEntry = tuplePair.b;
        Account account = tuplePair.a;
        Currency fromCurrency = transactionEntry.currency;
        TransactionEntry afterApplyingFxRate = fxCache.apply(fromCurrency)
                .map(fxEntry -> TransactionEntryBuilder.fromATransaction(transactionEntry)
                        .withTransactionAmount(transactionEntry.transactionAmount.multiply(BigDecimal.valueOf(fxEntry.rate)))
                        .build()
                ).orElseThrow(() -> new FXConversionException());
        return Tuple2.fromValues(tuplePair.a, afterApplyingFxRate);
    }

    private Tuple2<Account, TransactionEntry> lookupAccount(TransactionEntry transactionEntry, AccountCache accountCache) throws AccountNotFoundException {
        String lastName = transactionEntry.person.lastName;
        Date dob = transactionEntry.person.dob;
        String tfn = transactionEntry.person.tfn;

        final Account account = accountCache.findByAccountId(transactionEntry.accountId)
                .orElse(accountCache.findByLastNameAndTFNAndDOB(lastName, tfn, dob)
                        .orElse(accountCache.findByLastNameAndDOB(lastName, dob).orElseThrow(() -> new AccountNotFoundException())));
        return Tuple2.fromValues(account, transactionEntry);
    }

    private Either<RuntimeException, String> calculateValidationStatus(String line) {
        return areAllColumnsPresent(line, 9).flatMap(l -> areMandatoryColumnsPopulated(l, new int[]{0,1, 2,3}));
    }

}

class Tuple2<A, B>{
    public final A a;
    public final B b;

    public Tuple2(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public static <A, B> Tuple2<A, B> fromValues(A a, B b){
        return new Tuple2(a, b);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple2<?, ?> tuple2 = (Tuple2<?, ?>) o;
        return Objects.equals(a, tuple2.a) &&
                Objects.equals(b, tuple2.b);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }
}
