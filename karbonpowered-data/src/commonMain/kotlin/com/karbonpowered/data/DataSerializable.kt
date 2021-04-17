package com.karbonpowered.data

interface DataSerializable {
    fun toContainer(): DataContainer
}