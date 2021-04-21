package com.karbonpowered.api

interface ResourceKey {
    val namespace: String
    val value: String
}

interface ResourceKeyed {
    val resourceKey: ResourceKey
}