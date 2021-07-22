package com.karbonpowered.api.registry

interface ScopedRegistryHolder {
    val registryScope: RegistryScope
    val registries: RegistryHolder
}