package com.karbonpowered.api.state

interface State<S:State<S>> {
    override fun toString(): String
}