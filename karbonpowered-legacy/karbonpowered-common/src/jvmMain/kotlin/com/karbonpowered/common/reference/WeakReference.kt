package com.karbonpowered.common.reference

actual typealias WeakReference<T> = java.lang.ref.WeakReference<T>

actual val <T : Any> WeakReference<T>.value: T? get() = get()