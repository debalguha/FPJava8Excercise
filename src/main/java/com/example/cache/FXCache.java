package com.example.cache;

import com.example.domain.FxEntry;
import com.example.functions.Functions;

import java.io.File;
import java.util.Currency;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static com.example.functions.Functions.createFromLines;
import static com.google.common.base.Strings.emptyToNull;
import static java.util.Optional.ofNullable;
import static java.util.function.Function.identity;

public class FXCache implements Function<Currency, Optional<FxEntry>> {

    private final Map<Currency, FxEntry> cacheMap;

    public FXCache(File fxFile) {
        cacheMap = Functions.createFromFile(fxFile, this::createFxEntries, f -> f.from, identity());
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
