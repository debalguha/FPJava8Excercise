package com.example.cache;

import com.example.domain.FxEntry;
import com.example.functions.Functions;
import cyclops.control.Try;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.functions.Functions.fileDataSuplier;
import static com.example.functions.Functions.hasAllColumns;
import static com.google.common.base.Strings.emptyToNull;
import static cyclops.control.Try.withCatch;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;

public class FXCache implements Function<Currency, Optional<FxEntry>> {

    private final Map<Currency, FxEntry> cacheMap;

    public FXCache(File fxFile) {
        cacheMap = withCatch(fileDataSuplier(fxFile), IOException.class)
                .onFail(e -> {throw new RuntimeException("Unable to read fx file");})
                .map(lines -> createFxEntries(lines))
                .orElse(Collections.emptyList())
                .stream().collect(toMap(FxEntry::getFrom, Function.identity()));


    }

    private List<FxEntry> createFxEntries(List<String> lines) {
        return lines.stream().map(line -> fromLine(line))
                .flatMap(opt -> opt.map(v -> Stream.of(v)).orElse(Stream.empty()))
                /*.filter(opt -> opt.isPresent())
                .map(opt -> opt.get())*/
                .collect(Collectors.toList());
    }

    @Override
    public Optional<FxEntry> apply(Currency from) {
        return ofNullable(cacheMap.get(from));
    }



    private Optional<FxEntry> fromLine(String inputLine) {
        return ofNullable(emptyToNull(inputLine))
                .filter(s -> !s.startsWith("#"))
                .filter(s -> hasAllColumns(s, 3))
                //Chain more validations
                .map(s -> createFXEntry(s));
    }

    private FxEntry createFXEntry(String s) {
        String[] split = s.split(",", -1);
        return new FxEntry(Currency.getInstance(split[0]), Currency.getInstance("AUD"), Double.valueOf(split[1]), ofNullable(emptyToNull(split[2])));
    }

}
