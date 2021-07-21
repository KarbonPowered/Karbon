package com.karbonpowered.common.collection.concurrent.map

interface TripleIntObjectMap<T> : Map<Triple<Int, Int, Int>, T> {
    override val values: Collection<T>
    override val size: Int
        get() = values.size

    override fun isEmpty(): Boolean = values.isEmpty()
    override fun get(key: Triple<Int, Int, Int>): T? = get(key.first, key.second, key.third)
    operator fun set(x: Int, y: Int, z: Int, value: T): T?
    operator fun get(x: Int, y: Int, z: Int): T?
    operator fun iterator() = values.iterator()
    fun remove(x: Int, y: Int, z: Int): T?
    fun remove(x: Int, y: Int, z: Int, value: T): Boolean
}

inline fun <T> TripleIntObjectMap<T>.getOrPut(x: Int, y: Int, z: Int, defaultValue: () -> T): T {
    val value = get(x, y, z)
    return if (value == null) {
        val answer = defaultValue()
        set(x, y, z, answer)
        answer
    } else {
        value
    }
}

inline fun <T> TripleIntObjectMap<T>.getOrElse(x: Int, y: Int, z: Int, defaultValue: () -> T): T =
    get(x, y, z) ?: defaultValue()
