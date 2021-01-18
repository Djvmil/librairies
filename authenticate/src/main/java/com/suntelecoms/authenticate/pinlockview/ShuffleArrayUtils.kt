package com.suntelecoms.authenticate.pinlockview

import java.util.*

/**
 * @Author Moustapha S. Dieme ( Djvmil_ ) on 3/12/20.
 */
object ShuffleArrayUtils {
    /**
     * Shuffle an array
     *
     * @param array
     */
    fun shuffle(array: IntArray): IntArray {
        val length = array.size
        val random = Random()
        random.nextInt()
        for (i in 0 until length) {
            val change = i + random.nextInt(length - i)
            swap(array, i, change)
        }
        return array
    }

    private fun swap(array: IntArray, index: Int, change: Int) {
        val temp = array[index]
        array[index] = array[change]
        array[change] = temp
    }
}