package com.karbonpowered.data

interface ResourceKey {
    val namespace: String
    val value: String
}

interface ResourceKeyed {
    val resourceKey: ResourceKey
}