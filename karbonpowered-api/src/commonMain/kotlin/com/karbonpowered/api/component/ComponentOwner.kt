package com.karbonpowered.api.component

import kotlin.reflect.KClass

/**
 * Represents an object which may own components.
 */
interface ComponentOwner {
    val values: Collection<Component>

    /**
     * Adds the component of the specified type to the owner and returns it if it is not present.
     *
     * Otherwise, it returns the component of the specified type if there was one present.
     *
     * @param component whose component is to be added to the owner
     */
    fun <T : Component> addComponent(component: T, attach: Boolean = true): T

    fun <T : Component> getComponent(type: KClass<T>): T?

    fun <T : Component> detachComponent(type: KClass<T>, force: Boolean = false)
}

inline fun <reified T : Component> ComponentOwner.getComponent(): T? = getComponent(T::class)