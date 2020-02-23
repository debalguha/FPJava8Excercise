package com.example.monad.state;

import cyclops.data.HashMap;

import java.util.function.Function;

public class PortfolioProcessor {

    public static final Function<Message, Function<StockPortfolio, StockPortfolio>> transactionFunction =
        message -> switch (message.messageType()) {
            case TRANSACTION ->
                doTransaction((TransactionMessage) message);
            case CURRENCY ->
                changeCurrency((CurrencyMessage) message);
            default -> Function.identity();
        };

    private static Function<StockPortfolio, StockPortfolio> changeCurrency(CurrencyMessage message) {
        return portfolio -> new StockPortfolio(HashMap.fromMap(portfolio.stocks), message.currency, portfolio.transactionAmount);
    }

    private static Function<StockPortfolio, StockPortfolio> doTransaction(TransactionMessage message) {
        return switch(message.transactionType) {
            case BUY -> portfolio -> buy(message, portfolio);
            case SELL -> portfolio ->sell(message, portfolio);
        };
    }

    public static StockPortfolio buy(TransactionMessage message, StockPortfolio portfolio) {
        long newQuantity = portfolio.stocks.getOrElse(message.stock, 0L) + message.quantity;
        return new StockPortfolio(cyclops.data.HashMap.fromMap(portfolio.stocks).put(message.stock, newQuantity), portfolio.currency, portfolio.transactionAmount - message.amount);
    }
    public static StockPortfolio sell(TransactionMessage message, StockPortfolio portfolio) {
        long newQuantity = portfolio.stocks.getOrElse(message.stock, 0L) - message.quantity;
        return new StockPortfolio(cyclops.data.HashMap.fromMap(portfolio.stocks).put(message.stock, newQuantity), portfolio.currency, portfolio.transactionAmount + message.amount);
    }

    public final StockPortfolio process(Message message){
        return null;
    }
}
