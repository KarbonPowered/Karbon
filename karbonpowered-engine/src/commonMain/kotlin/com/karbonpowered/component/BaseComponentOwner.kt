package com.karbonpowered.component

import com.karbonpowered.api.component.Component
import com.karbonpowered.api.component.ComponentOwner
import com.karbonpowered.logging.Logger
import kotlinx.atomicfu.locks.reentrantLock
import kotlinx.atomicfu.locks.withLock
import kotlin.reflect.KClass

open class BaseComponentOwner : ComponentOwner {
    private val componentsLock = reentrantLock()
    private val components = mutableMapOf<KClass<out Component>, Component>()
    override val values: Collection<Component>
        get() = componentsLock.withLock {
            components.values.toList()
        }

    override fun <T : Component> addComponent(component: T, attach: Boolean): T {
        val componentClass = component::class
        componentsLock.withLock {
            var currentComponent = components[componentClass] as? T
            if (currentComponent != null) {
                return currentComponent
            }
            currentComponent = component
            try {
                attachComponent(componentClass, currentComponent, attach)
            } catch (e: Exception) {
                Logger.error("Error while attaching component $componentClass", e)
            }
            return currentComponent
        }
    }

    override fun <T : Component> getComponent(type: KClass<T>): T? =
        (components[type] ?: findComponent(type)) as? T

    override fun <T : Component> detachComponent(type: KClass<T>, force: Boolean) {
        componentsLock.withLock {
            val component = components[type] as? T

            if (component != null && (component.isDetachable || force)) {
                components.remove(type)
                try {
                    component.onDetached()
                } catch (e: Exception) {
                    Logger.error("Error detaching component $type from holder", e)
                }
            }
        }
    }

    protected fun attachComponent(key: KClass<out Component>, component: Component, attach: Boolean) {
        if (component.attachTo(this)) {
            components[key] = component
            if (attach) {
                try {
                    component.onAttached()
                } catch (e: Exception) {
                    // Remove the component from the component map if onAttached can't be
                    // called, pass exception to next catch block.
                    components.remove(key)
                    throw e
                }
            }
        }
    }

    private fun <T : Any> findComponent(type: KClass<T>): T? = componentsLock.withLock {
        components.values.find { type.isInstance(it) }
    } as? T
}