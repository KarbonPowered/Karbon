package com.karbonpowered.io

interface Serializer<I, O, R> {
    fun deserialize(data: R): O

    fun serialize(data: I): R
}