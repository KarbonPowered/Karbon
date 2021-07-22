package com.karbonpowered.api.event

import kotlin.reflect.KClass

interface Cause : Iterable<Any> {
    val context: EventContext
    val root: Any

    fun <T : Any> first(target: KClass<T>): T?
    fun <T : Any> last(target: KClass<T>): T?
    fun before(target: KClass<*>): Any?
    fun after(target: KClass<*>): Any?
    fun containsType(target: KClass<*>): Boolean
    operator fun contains(target: Any): Boolean
    fun <T : Any> allOff(target: KClass<T>): Collection<T>

    interface Factory {
        fun create(context: EventContext, cause: Any): Cause
    }

    companion object {
        lateinit var factory: (EventContext, Any) -> Cause

        operator fun invoke(context: EventContext, cause: Any): Cause = factory(context, cause)
    }
}