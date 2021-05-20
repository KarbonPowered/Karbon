package com.karbonpowered.common.collection.concurrent

interface TripleIntObjectMap<T> {
    val values: Collection<T>
    operator fun get(x: Int, y: Int, z: Int): T?
    fun remove(x: Int, y: Int, z: Int): T?
    fun remove(x: Int, y: Int, z: Int, value: T): Boolean
    fun getOrPut(x: Int, y: Int, z: Int, value: () -> T): T
}