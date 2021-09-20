package com.suntelecoms.timeline.models;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 10/12/19.
 */
public class Sequence {

    private static final AtomicInteger counter = new AtomicInteger();

    public static int nextValue() {
        return counter.getAndIncrement();
    }
}