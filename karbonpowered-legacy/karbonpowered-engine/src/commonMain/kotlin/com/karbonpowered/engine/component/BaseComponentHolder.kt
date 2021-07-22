package com.karbonpowered.engine.component

import com.karbonpowered.engine.KarbonEngine
import kotlinx.atomicfu.locks.reentrantLock
import kotlinx.atomicfu.locks.withLock
import kotlin.reflect.KClass

class BaseComponentHolder(
    val engine: KarbonEngine
) : ComponentHolder {
    val components = mutableMapOf<KClass<out Component>, Component>()
    val lock = reentrantLock()

    override fun iterator(): Iterator<Component> = components.values.iterator()

    override fun <T : Component> add(key: KClass<out T>, component: T, attach: Boolean): T {
        lock.withLock {
            val oldComponent = components[key] as? T
            if (oldComponent != null) {
                return oldComponent
            }

            try {
                attachComponent(key, component, attach)
            } catch (e: Exception) {
                engine.error("Error while attaching component $component: ", e)
            }
            return component
        }
    }

    override fun <T : Component> get(type: KClass<T>, factory: () -> T): T =
        (components[type] ?: findComponent(type) ?: factory()) as T

    private fun attachComponent(key: KClass<out Component>, component: Component, attach: Boolean) {
        if (component.attachTo(this)) {
            components[key] = component
            if (attach) {
                try {
                    component.onAttached()
                } catch (e: Exception) {
                    // Remove the component from the component map if onAttached can't be
                    // called, pass exception to next catch block.
                    components.remove(key)
                    throw e;
                }
            }
        }
    }

    private fun <T : Component> findComponent(type: KClass<T>) = lock.withLock {
        find { it::class.isInstance(type) }
    }
}