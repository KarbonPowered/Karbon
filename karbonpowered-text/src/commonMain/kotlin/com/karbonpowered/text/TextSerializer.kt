package com.karbonpowered.text

interface TextSerializer<T> {
    fun serialize(text: Text): T

    fun deserialize(t: T): Text
}