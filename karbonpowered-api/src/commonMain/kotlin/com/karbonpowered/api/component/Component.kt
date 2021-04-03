package com.karbonpowered.api.component

import com.karbonpowered.api.tick.Tickable
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

interface Component : Tickable {
    val owner: ComponentOwner

    override fun canTick(): Boolean = true

    val isDetachable: Boolean
        get() = true

    @OptIn(ExperimentalTime::class)
    override suspend fun onTick(duration: Duration) {
    }

    /**
     * Attaches to a component owner.
     *
     * @param owner the component owner to attach to
     * @return true if successful
     */
    fun attachTo(owner: ComponentOwner): Boolean

    /**
     * Called when this component is attached to a owner.
     */
    fun onAttached() {}

    /**
     * Called when this component is detached from a owner.
     */
    fun onDetached() {}
}