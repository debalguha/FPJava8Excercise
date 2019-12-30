package com.example.cache;

import com.example.domain.FxEntry;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;

import static com.example.functions.Functions.*;
import static com.google.common.base.Strings.emptyToNull;
import static cyclops.control.Try.withCatch;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;

public class FXCache implements Function<Currency, Optional<FxEntry>> {

    private final Map<Currency, FxEntry> cacheMap;

    public FXCache(File fxFile) {
        cacheMap = withCatch(fileDataSupplier(fxFile), IOException.class)
                .onFail(e -> {throw new RuntimeException("Unable to read fx file");})
                .map(lines -> createFxEntries(lines))
                .orElse(Collections.emptyList())
                .stream().collect(toMap(FxEntry::getFrom, Function.identity()));
    }

    private List<FxEntry> createFxEntries(List<String> lines) {
        return createFromLines(lines, s -> createFXEntry(s));
    }

    @Override
    public Optional<FxEntry> apply(Currency from) {
        return ofNullable(cacheMap.get(from));
    }

    private FxEntry createFXEntry(String s) {
        String[] split = s.split(",", -1);
        return new FxEntry(Currency.getInstance(split[0]), Currency.getInstance("AUD"), Double.valueOf(split[1]), ofNullable(emptyToNull(split[2])));
    }

}
