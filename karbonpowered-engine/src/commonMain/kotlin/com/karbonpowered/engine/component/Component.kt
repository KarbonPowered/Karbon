package com.karbonpowered.engine.component

import com.karbonpowered.engine.util.tick.Tickable

abstract class Component : Tickable {
    private var _owner: ComponentHolder? = null
    val owner: ComponentHolder get() = checkNotNull(_owner) { "Trying to access the owner of this component before it was attached" }

    fun attachTo(owner: BaseComponentHolder): Boolean {
        _owner = owner
        return true
    }

    open fun onAttached() {}
}