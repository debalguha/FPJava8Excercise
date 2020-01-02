package com.example.cache;

import com.example.domain.FxEntry;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.example.functions.Functions.*;
import static java.util.Optional.ofNullable;
import static java.util.function.Function.identity;

public class FXCache implements Function<Currency, Optional<FxEntry>> {

    private final Map<Currency, FxEntry> cacheMap;

    public FXCache(@Nonnull File fxFile) {
        Objects.requireNonNull(fxFile, "Input file must be not null");
        cacheMap = createFromFile(() -> readFileToLines(fxFile), this::createFxEntries, f -> f.from, identity());
    }

    private List<FxEntry> createFxEntries(List<String> lines) {
        Predicate<String> validationPredicate = nullOrEmptyLinesPredicate.and(commentedLinesPredicate).and(columnNumberPredicateFunc.apply(3));
        return createFromLines(lines, s -> createFXEntry(s), validationPredicate);
    }

    @Override
    public Optional<FxEntry> apply(@Nonnull  Currency from) {
        Objects.requireNonNull(from, "From currency must not be null");
        return ofNullable(cacheMap.get(from));
    }

}
