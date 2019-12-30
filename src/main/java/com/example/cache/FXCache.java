package com.example.cache;

import com.example.domain.FxEntry;
import com.example.functions.Functions;

import java.io.File;
import java.util.Currency;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static com.example.functions.Functions.*;
import static java.util.Optional.ofNullable;
import static java.util.function.Function.identity;

public class FXCache implements Function<Currency, Optional<FxEntry>> {

    private final Map<Currency, FxEntry> cacheMap;

    public FXCache(File fxFile) {
        cacheMap = createFromFile(fxFile, this::createFxEntries, f -> f.from, identity());
    }

    private List<FxEntry> createFxEntries(List<String> lines) {
        return createFromLines(lines, s -> createFXEntry(s));
    }

    @Override
    public Optional<FxEntry> apply(Currency from) {
        return ofNullable(cacheMap.get(from));
    }

}
