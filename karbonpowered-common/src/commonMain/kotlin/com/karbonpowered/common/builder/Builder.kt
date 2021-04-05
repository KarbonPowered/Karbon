package com.karbonpowered.common.builder

interface Builder<T, B : Builder<T, B>> {
    fun build(): T

    fun reset(): B = this as B
}