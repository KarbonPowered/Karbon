package com.karbonpowered.api.fluid

import com.karbonpowered.api.registry.DefaultedRegistryValue
import com.karbonpowered.api.state.StateContainer
import com.karbonpowered.data.DataHolder

interface FluidType : DefaultedRegistryValue, StateContainer<FluidState>, DataHolder.Immutable<FluidType>