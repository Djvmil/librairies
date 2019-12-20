package com.suntelecoms.td.dynamic_form;

import java.util.concurrent.atomic.AtomicInteger;

public class Sequence {

    private static final AtomicInteger counter = new AtomicInteger();

    public static int nextValue() {
        return counter.getAndIncrement();
    }
}