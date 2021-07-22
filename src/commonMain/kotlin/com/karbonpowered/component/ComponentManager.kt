package com.karbonpowered.component

import kotlin.reflect.KClass

interface ComponentManager<H> {
    fun <C : Component> addComponent(holder: H, instance: C)

    fun <C : Component> getComponent(holder: H, type: KClass<C>): C?

    fun hasComponent(holder: H, type: KClass<out Component>): Boolean

    fun <C : Component> removeComponent(holder: H, type: KClass<C>)
}