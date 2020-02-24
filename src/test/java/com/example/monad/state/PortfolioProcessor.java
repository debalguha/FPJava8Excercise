package com.example.monad.state;

import cyclops.data.HashMap;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import static com.example.monad.state.Tuple2.tuple2;
import static java.util.Collections.emptyList;

public class PortfolioProcessor<S, A> {
    public static final Function<Message, Function<StockPortfolio, StockPortfolio>> update =
        message -> switch (message.messageType()) {
            case TRANSACTION ->
                doTransaction((TransactionMessage) message);
            case CURRENCY ->
                changeCurrency((CurrencyMessage) message);
            default -> Function.identity();
        };

    public static <S, A> State<S, A> unit(A a) {
        return new State(s -> tuple2(s, a));
    }
    public static final <S, A> State<S, List<A>> sequence(List<State<S, A>> stateActions) {

        final BiFunction<List<A>, A, List<A>> map2AccumulatorFunction = (as, a) -> {
            as.add(a);
            return as;
        };

        final BinaryOperator<State<S, List<A>>> unsupportedBinaryOperationInSequentialStream = (sListState, sListState2) -> {
            throw new UnsupportedOperationException("This stream is strictly sequential. It can not be operated in parallel.");
        };

        return stateActions.stream().reduce(unit(emptyList()), (sListState, saState) -> sListState.map2(saState, map2AccumulatorFunction), unsupportedBinaryOperationInSequentialStream);
    }

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

    public final StockPortfolio process(Message message, StockPortfolio portfolio){
        return update.apply(message).apply(portfolio);
    }

    public final StockPortfolio processTwoMessages(StockPortfolio startState, Message message1, Message message2) {
        final StockPortfolio nextState = update.apply(message1).apply(startState);
        return update.apply(message2).apply(nextState);
    }

    public final StockPortfolio processMany(StockPortfolio startState, List<Message> messages) {
        final StockPortfolio finalState = null;
        return messages.stream().reduce(startState, (portfolio, message) -> update.apply(message).apply(portfolio), new BinaryOperator<StockPortfolio>() {
            @Override
            public StockPortfolio apply(StockPortfolio portfolio, StockPortfolio portfolio2) {
                return null;
            }
        });
    }

}
