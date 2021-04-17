package com.karbonpowered.api.world.location

import com.karbonpowered.data.SerializableDataHolder

interface LocatableBlock : SerializableDataHolder.Immutable<LocatableBlock>, Locatable {
}