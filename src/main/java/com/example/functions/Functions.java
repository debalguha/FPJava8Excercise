package com.example.functions;

import com.example.cache.Person;
import com.example.domain.Account;
import com.example.domain.FxEntry;
import com.example.domain.TransactionEntry;
import cyclops.control.Try;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Strings.emptyToNull;
import static cyclops.control.Try.withCatch;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

public class Functions {
    public static Try.CheckedSupplier<List<String>, IOException> fileDataSupplier(File inputFile) {
        return () -> Files.readAllLines(inputFile.toPath());
    }
    public static boolean hasAllColumns(String s, int numcColumns) {
        return s.split(",").length == numcColumns;
    }

    public static <T, K, V> Map<K, V> createFromFile(File fxFile, Function<List<String>, List<T>> lineMapperFunc, Function<T, K> keyFunc, Function<T, V> valueFunc) {
        return withCatch(fileDataSupplier(fxFile), IOException.class)
                .onFail(e -> {throw new RuntimeException("Unable to read fx file");})
                .map(lineMapperFunc)
                .orElse(Collections.emptyList())
                .stream().collect(toMap(keyFunc, valueFunc));
    }

    public static <T> List<T> createFromLines(List<String> lines, Function<String, T> func) {
        return lines.stream().map(line -> fromLine(line, func))
                .flatMap(opt -> opt.map(v -> Stream.of(v)).orElse(Stream.empty()))
                /*.filter(opt -> opt.isPresent())
                .map(opt -> opt.get())*/
                .collect(Collectors.toList());
    }

    public static <T> Optional<T> fromLine(String inputLine, Function<String, T> func) {
        return ofNullable(emptyToNull(inputLine))
                .filter(s -> !s.startsWith("#"))
                .filter(s -> hasAllColumns(s, 3))
                //Chain more validations
                .map(func);
    }

    public static Account createAccount(String s) {
        String[] split = s.split(",", -1);
        long accountId = Long.valueOf(split[0]);
        BigDecimal balance = new BigDecimal(split[1]);
        return new Account(accountId, balance, new Person(stream(split).skip(2).collect(joining())));
    }

    public static FxEntry createFXEntry(String s) {
        String[] split = s.split(",", -1);
        return new FxEntry(Currency.getInstance(split[0]), Currency.getInstance("AUD"), Double.valueOf(split[1]), ofNullable(emptyToNull(split[2])));
    }

    public static TransactionEntry createTransactionEntry(String s) {
        String split[] = s.split(",", -1);

    }
}
