package com.suntelecoms.djamil.dynamic_form.models;
/**
 *
 *   Djvmil 19/12/2020
 *
 **/
import java.util.concurrent.atomic.AtomicInteger;

public class Sequence {

    private static final AtomicInteger counter = new AtomicInteger();

    public static int nextValue() {
        return counter.getAndIncrement();
    }
}