package com.karbonpowered.component

import com.karbonpowered.api.component.Component
import com.karbonpowered.api.component.ComponentOwner
import com.karbonpowered.logging.Logger
import kotlinx.atomicfu.locks.reentrantLock
import kotlinx.atomicfu.locks.withLock
import kotlin.reflect.KClass

open class BaseComponentOwner : ComponentOwner {
    private val componentsLock = reentrantLock()
    private val componentsMap = mutableMapOf<KClass<out Component>, Component>()
    override val components: Collection<Component>
        get() = componentsLock.withLock {
            componentsMap.values
        }

    override fun <T : Component> addComponent(component: T, attach: Boolean): T {
        val componentClass = component::class
        componentsLock.withLock {
            var currentComponent = componentsMap[componentClass] as? T
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
        (componentsMap[type] ?: findComponent(type)) as? T

    override fun <T : Component> detachComponent(type: KClass<T>, force: Boolean) {
        componentsLock.withLock {
            val component = componentsMap[type] as? T

            if (component != null && (component.isDetachable || force)) {
                componentsMap.remove(type)
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
            componentsMap[key] = component
            if (attach) {
                try {
                    component.onAttached()
                } catch (e: Exception) {
                    // Remove the component from the component map if onAttached can't be
                    // called, pass exception to next catch block.
                    componentsMap.remove(key)
                    throw e
                }
            }
        }
    }

    private fun <T : Any> findComponent(type: KClass<T>): T? = componentsLock.withLock {
        componentsMap.values.find { type.isInstance(it) }
    } as? T
}