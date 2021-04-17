package com.karbonpowered.data

interface DataHolder : ValueContainer {
    interface Mutable : DataHolder

    interface Immutable<I : Immutable<I>> : DataHolder
}