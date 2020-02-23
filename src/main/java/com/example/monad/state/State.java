package com.example.monad.state;

import java.util.function.BiFunction;
import java.util.function.Function;

import static com.example.monad.state.Tuple2.tuple2;

public class State<S, A> {
    public final Function<S, Tuple2<S, A>> action;

    public State(Function<S, Tuple2<S, A>> action) {
        this.action = action;
    }

    public static <S, A> State<S, A> unit(A v) {
        return new State(s -> tuple2(s, v));
    }
    
    public <B> State<S, B> map(Function<A, B> f){
        return new State<>(s -> {
            final Tuple2<S, A> s1 = action.apply(s);
            return tuple2(s1.t, f.apply(s1.v));
        });
    }

    public <B> State<S, B> flatMap(Function<A, State<S, B>> f) {
        return new State<>(s -> {
            final Tuple2<S, A> s1 = action.apply(s);
            return f.apply(s1.v).action.apply(s1.t);
        });
    }

    public <B, C> State<S, C> map2(State<S, B> bState, BiFunction<A, B, C> f) {
        return new State<>(s -> {
            final Tuple2<S, A> state1 = action.apply(s);
            final Tuple2<S, B> state2 = bState.action.apply(state1.t);
            return tuple2(state2.t, f.apply(state1.v, state2.v));
        });
    }
}
