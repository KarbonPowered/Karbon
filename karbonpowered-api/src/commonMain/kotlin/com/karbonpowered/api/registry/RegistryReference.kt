package com.karbonpowered.api.registry

interface RegistryReference<T : Any> : RegistryKey<T> {
    operator fun get(holder: RegistryHolder): T
    operator fun get(holder: RegistryHolder, alternatives: Set<RegistryHolder>): T
    fun find(holder: RegistryHolder): T? = holder.findRegistry(registry)?.findValue(this)
}