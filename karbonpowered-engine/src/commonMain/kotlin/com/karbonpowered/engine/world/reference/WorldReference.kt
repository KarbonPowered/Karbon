package com.karbonpowered.engine.world.reference

import com.karbonpowered.common.reference.WeakReference
import com.karbonpowered.data.ResourceKey
import com.karbonpowered.engine.world.KarbonWorld
import com.karbonpowered.engine.world.KarbonWorldManager
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
open class WorldReference(
    val identifier: ResourceKey,
    @Transient
    private var reference: WeakReference<KarbonWorld>? = null
) {
    constructor(world: KarbonWorld) : this(world.identifier, WeakReference(world))

    fun get(): KarbonWorld? =
        reference?.get()?.takeIf { it.isLoaded }

    open fun refresh(manager: KarbonWorldManager): KarbonWorld? {
        get()?.let { return it }
        val world = manager.world(identifier)
        reference = world?.let { WeakReference(it) }
        return world
    }

    override fun hashCode(): Int = identifier.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as WorldReference

        if (identifier != other.identifier) return false

        return true
    }

    companion object {
        val EMPTY = object : WorldReference(ResourceKey("", "")) {
            override fun refresh(manager: KarbonWorldManager): KarbonWorld? = null
        }
    }
}