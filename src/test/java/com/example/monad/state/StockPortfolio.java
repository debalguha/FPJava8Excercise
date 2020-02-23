package com.example.monad.state;

import cyclops.data.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public class StockPortfolio {
    public static Map<String, Double> conversionChart = new HashMap<>();
    static {
        conversionChart.put("AUD", Double.parseDouble("48.25"));
        conversionChart.put("USD", Double.parseDouble("68.66"));
        conversionChart.put("GBP", Double.parseDouble("92.42"));
    }

    public final ImmutableMap<String, Long> stocks;
    public final String currency;
    public final Double transactionAmount;

    public StockPortfolio(ImmutableMap<String, Long> stocks, String currency, Double transactionAmount) {
        this.stocks = stocks;
        this.currency = currency;
        this.transactionAmount = transactionAmount;
    }
}
