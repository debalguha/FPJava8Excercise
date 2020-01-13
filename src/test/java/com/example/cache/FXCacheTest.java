package com.example.cache;

import com.example.domain.FxEntry;
import org.junit.Test;

import java.io.File;
import java.util.Currency;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FXCacheTest {
    @Test
    public void shouldBeAbleToReadAndConstructFxCache() {
        final FXCache fxCache = new FXCache(new File("src/test/resources/fx-rate.txt"));
        assertNotNull(fxCache);
        final Optional<FxEntry> eurToAud = fxCache.apply(Currency.getInstance("EUR"));
        assertEquals(1.6d, eurToAud.get().rate, 0);
    }
}
