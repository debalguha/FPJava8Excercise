package com.example.functions;

import com.example.cache.Person;
import com.example.constructs.Try;
import com.example.domain.Account;
import com.example.domain.FxEntry;
import com.example.domain.TransactionEntry;
import com.example.domain.TransactionType;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Currency;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.example.domain.AccountBuilder.fromAccount;
import static com.google.common.base.Strings.emptyToNull;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

public class Functions {
    private static Predicate<String> nullOrEmptyLinesPredicate = s -> ofNullable(s).orElse("").isEmpty();
    public static Predicate<String> notNullOrEmptyLinesPredicate = nullOrEmptyLinesPredicate.negate();
    private static Predicate<String> commentedLinesPredicate = s -> s.startsWith("#");
    public static Predicate<String> unCommentedLinesPredicate = commentedLinesPredicate.negate();
    public static Function<String, Predicate<Account>> accountPredicateByLastNameFunc = lastName -> a -> lastName.equals(a.person.lastName);
    public static Function<LocalDate, Predicate<Account>> accountPredicateByDobFunc = dob -> a -> dob.atStartOfDay(ZoneId.systemDefault()).isEqual(a.person.dob.atStartOfDay(ZoneId.systemDefault()));
    public static Function<String, Predicate<Account>> accountPredicateByTfnFunc = tfn -> a -> tfn.equals(a.person.tfn);
    public static Function<int[], Predicate<String>> mandatoryColumnsPredicateFunc = intArray -> s -> {
        String[] split = ofNullable(s).orElse("").split(",", -1);
        return stream(intArray).allMatch(i -> split.length > i && !split[i].isEmpty());
    };
    public static Function<Integer, Predicate<String>> columnNumberPredicateFunc = i -> s -> s.split(",", -1).length == i;
    public static BinaryOperator<Account> accountAggregator = (a, b) -> fromAccount(a).balance(a.balance.add(b.balance)).build();

    public static List<String> readFileToLines(File inputFile) {
        try {
            return Files.readAllLines(inputFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T, K, V> Map<K, V> createFromFile(Supplier<List<String>> fileDataSupplier, Function<List<String>, List<T>> lineMapperFunc, Function<T, K> keyFunc, Function<T, V> valueFunc) {

        return Try.doTry(fileDataSupplier)
                .map(lineMapperFunc)
                .orElse(emptyList())
                .stream().collect(toMap(keyFunc, valueFunc));
    }

    public static <T> List<T> createFromLines(List<String> lines, Function<String, T> func, Predicate<String> validationPredicate) {
        return lines.stream()
                .skip(1)
                .filter(validationPredicate)
                .map(func)
                .collect(Collectors.toList());
    }

    public static Optional<Account> createAccount(String s) {
        String[] split = s.split(",", -1);
        long accountId = Long.valueOf(split[0]);
        BigDecimal balance = new BigDecimal(split[1]);
        return Try.doTry(() -> new Account(accountId, balance, new Person(stream(split).skip(2).collect(joining(","))))).toOptional();
    }

    public static FxEntry createFXEntry(String s) {
        String[] split = s.split(",", -1);
        return new FxEntry(Currency.getInstance(split[0]), Currency.getInstance(split[1]), Double.valueOf(split[2]), ofNullable(emptyToNull(split[3])));
    }

    public static TransactionEntry createTransactionEntry(String s) {
        String split[] = s.split(",", -1);
        Person person = new Person(stream(split).skip(5).collect(joining()));
        return new TransactionEntry(Long.valueOf(split[0]), Long.valueOf(split[1]), new BigDecimal(split[2]), TransactionType.valueOf(split[3]),
                Currency.getInstance(split[4]), person);
    }
}
