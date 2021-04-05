package com.karbonpowered.api.state

interface State<S : State<S>> {
    override fun toString(): String

    interface Builder<S : State<S>, B : Builder<S, B>> {
        /**
         * Populates this builder with the representation of a [State] serialized in the [String].
         *
         * @param id The serialized string
         * @return This builder, for chaining
         */
        fun fromString(id: String): B
    }
}