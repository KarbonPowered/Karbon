package com.karbonpowered.api.context

interface Context : Map.Entry<String, String> {
    override val key: String
    override var value: String
}