package com.karbonpowered.data

interface Value<E> {
    val value: E
    val key: Key<out Value<E>>

    fun asMutable(): Mutable<E>
    fun asMutableCopy(): Mutable<E>
    fun asImmutable(): Immutable<E>

    interface Mutable<E> : Value<E> {
        fun set(value: E): Mutable<E>

        fun transform(function: (E) -> E): Mutable<E>

        override fun asMutable(): Mutable<E> = this

        override fun asMutableCopy(): Mutable<E> = copy()

        fun copy(): Mutable<E>
    }

    interface Immutable<E> : Value<E> {
        fun with(value: E): Immutable<E>

        fun transform(function: (E) -> E): Immutable<E>

        override fun asMutableCopy(): Mutable<E> = asMutableCopy()

        override fun asImmutable(): Immutable<E> = this
    }
}