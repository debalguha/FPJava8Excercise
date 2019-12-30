package com.example.domain;

import com.google.common.base.Strings;

import java.util.Currency;
import java.util.Objects;
import java.util.Optional;

public class FxEntry {
    public final Currency from;
    public final Currency to;
    public final double rate;
    public final Optional<String> via;

    public FxEntry(Currency from, Currency to, double rate, Optional<String> via) {
        this.from = from;
        this.to = to;
        this.rate = rate;
        this.via = via;
    }

    public Currency getFrom() {
        return from;
    }

    public Currency getTo() {
        return to;
    }

    public double getRate() {
        return rate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FxEntry fxEntry = (FxEntry) o;
        return Objects.equals(from, fxEntry.from) &&
                Objects.equals(to, fxEntry.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    @Override
    public String toString() {
        return "FxEntry{" +
                "from=" + from +
                ", to=" + to +
                ", rate=" + rate +
                '}';
    }
}
