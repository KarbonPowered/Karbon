package com.karbonpowered.data

interface CopyableDataHolder : DataHolder {
    interface Mutable : CopyableDataHolder, DataHolder.Mutable
}