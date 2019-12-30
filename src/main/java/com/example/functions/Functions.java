package com.example.functions;

import com.example.domain.FxEntry;
import com.google.common.base.Function;
import cyclops.control.Try;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Strings.emptyToNull;
import static java.util.Optional.ofNullable;

public class Functions {
    public static Try.CheckedSupplier<List<String>, IOException> fileDataSuplier(File inputFile) {
        return () -> Files.readAllLines(inputFile.toPath());
    }
    public static boolean hasAllColumns(String s, int numcColumns) {
        return s.split(",").length == numcColumns;
    }
    public static <T> Optional<T> fromLine(String inputLine, Function<String, T> func) {
        return ofNullable(emptyToNull(inputLine))
                .filter(s -> !s.startsWith("#"))
                .filter(s -> hasAllColumns(s, 3))
                //Chain more validations
                .map(func);
    }
}
