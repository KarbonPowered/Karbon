package com.karbonpowered.api.datapack

import com.karbonpowered.data.ResourceKeyed

interface DataPackSerializable : ResourceKeyed {
    val type: DataPackType<*>
}