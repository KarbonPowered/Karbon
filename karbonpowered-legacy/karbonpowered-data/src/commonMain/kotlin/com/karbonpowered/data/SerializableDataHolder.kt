package com.karbonpowered.data

interface SerializableDataHolder : DataSerializable {
    interface Mutable : SerializableDataHolder, CopyableDataHolder.Mutable

    interface Immutable<I : Immutable<I>> : SerializableDataHolder, DataHolder.Immutable<I>
}