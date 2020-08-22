package com.suntelecoms.authenticate.andrognito.pinlockview;

import java.util.Random;

/**
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 3/12/20.
 */
public class ShuffleArrayUtils {

    /**
     * Shuffle an array
     *
     * @param array
     */
    static int[] shuffle(int[] array) {
        int length = array.length;
        Random random = new Random();
        random.nextInt();

        for (int i = 0; i < length; i++) {
            int change = i + random.nextInt(length - i);
            swap(array, i, change);
        }
        return array;
    }

    private static void swap(int[] array, int index, int change) {
        int temp = array[index];
        array[index] = array[change];
        array[change] = temp;
    }
}