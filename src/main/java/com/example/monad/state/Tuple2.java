package com.example.monad.state;

public class Tuple2<T, V> {
    public final T t;
    public final V v;

    public Tuple2(T t, V v) {
        this.t = t;
        this.v = v;
    }

    public static <T, V> Tuple2<T, V> tuple2(T t , V v){
        return new Tuple2(t, v);
    }

}
