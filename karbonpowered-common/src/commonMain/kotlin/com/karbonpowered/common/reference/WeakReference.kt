package com.karbonpowered.common.reference

expect class WeakReference<T : Any>(
    referred: T
) {
    fun clear()

    fun get(): T?
}

expect val <T : Any> WeakReference<T>.value: T?