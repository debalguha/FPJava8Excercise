package com.example.functions;

import cyclops.control.Try;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Strings.emptyToNull;
import static cyclops.control.Try.withCatch;
import static java.util.Optional.ofNullable;
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
}
