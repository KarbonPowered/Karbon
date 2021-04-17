package com.karbonpowered.api.datapack

import com.karbonpowered.api.ResourceKeyed

interface DataPackSerializable : ResourceKeyed {
    val type: DataPackType<*>
}