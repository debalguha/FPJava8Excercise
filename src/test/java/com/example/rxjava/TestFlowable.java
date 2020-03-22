package com.example.rxjava;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.flowables.GroupedFlowable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import static io.reactivex.rxjava3.schedulers.Schedulers.from;
import static java.util.concurrent.Executors.newFixedThreadPool;

public class TestFlowable {
    //@Test
    public void testBasicGroupByFlowable() throws InterruptedException {
        Flowable<GroupedFlowable<String, Integer>> groupedFlowable =
                Flowable.range(1, 100).groupBy(integer -> {
                    if (integer % 2 == 0) return "Even";
                    else return "Odd";
                });

        groupedFlowable.subscribe(g -> g.subscribe(x -> System.out.println("g:" + g.getKey() + ", value:" + x)));
        Thread.sleep(4000);
    }

    //@Test
    public void testBasicGroupByFlowableReduceIntoMultiMap() {
        Flowable<GroupedFlowable<String, Integer>> groupedFlowable =
                Flowable.range(1, 100).groupBy(integer -> {
                    if (integer % 2 == 0) return "Even";
                    else return "Odd";
                });

        Map<String, Single<List<Integer>>> result = new HashMap<>();

        groupedFlowable.subscribe(g -> result.put(g.getKey(), g.toList()));

        System.out.println(result.get("Even").blockingGet());
        System.out.println(result.get("Odd").blockingGet());
    }

    @Test
    public void testBasicGroupByFlowableReduceIntoMultiMapWithIterable() throws Exception {
        /*Flowable<GroupedFlowable<String, Integer>> groupedFlowable =
                Flowable.range(1, 10).groupBy(integer -> {
                    if (integer % 2 == 0) return "Even";
                    else return "Odd";
                });

        Multimap<String, Integer> result = HashMultimap.create();
        Map<String, Iterable<Integer>> resultIter = new HashMap<>();

        groupedFlowable.subscribe(g -> {
            String key = g.getKey();
            g.subscribe(i -> result.put(key, i));
        });


//        result.get("Even").forEach(System.out:: println);
//        result.get("Odd").forEach(System.out:: println);


        System.out.println("######");*/
        Flowable.range(1, 10).observeOn(Schedulers.computation()).doOnComplete(() -> System.out.println("Subs")).subscribe(i -> System.out.print(i));
        //Flowable.range(1, 10).observeOn(from(newFixedThreadPool(10))).doOnComplete(() -> System.out.println("Subs")).subscribe(i -> System.out.print(i));
        System.out.println();
        Flowable.range(1, 10).observeOn(from(newFixedThreadPool(10))).doOnComplete(() -> System.out.println("Blocks")).blockingForEach(i -> System.out.print(i));
        /*groupedFlowable.subscribe(g -> resultIter.put(g.getKey(), g.blockingIterable(10)));
        groupedFlowable.subscribe(g -> g.blockingForEach(i -> System.out.print(i)));
        resultIter.get("Even").forEach(System.out:: println);
        resultIter.get("Odd").forEach(System.out:: println);*/
        Thread.sleep(100000l);
        System.out.println("Ending");

    }
}
