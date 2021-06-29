package com.karbonpowered.common.hash

object IntPairHashed {
    /**
     * Creates a long key from 2 ints
     *
     * @param key1 an `int` value
     * @param key2 an `int` value
     * @return a long which is the concatenation of key1 and key2
     */
    fun key(key1: Int, key2: Int): Long = key1.toLong() shl 32 or key2.toLong() and 0xFFFFFFFFL

    /**
     * Gets the first 32-bit integer value from an long key
     *
     * @param key to get from
     * @return the first 32-bit integer value in the key
     */
    fun key1(key: Long): Int = (key shr 32 and 0xFFFFFFFFL).toInt()

    /**
     * Gets the second 32-bit integer value from an long key
     *
     * @param key to get from
     * @return the second 32-bit integer value in the key
     */
    fun key2(key: Long): Int = (key and 0xFFFFFFFFL).toInt()
}