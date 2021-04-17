package com.karbonpowered.api.audience

import com.karbonpowered.api.Karbon
import com.karbonpowered.api.ResourceKey
import com.karbonpowered.api.registry.DefaultedRegistryReference
import com.karbonpowered.api.registry.DefaultedRegistryValue
import com.karbonpowered.api.registry.RegistryKey
import com.karbonpowered.api.registry.RegistryTypes

interface MessageType : DefaultedRegistryValue

object MessageTypes {
    val ACTION_BAR by key(ResourceKey.karbon("action_bar"))
    val CHAT by key(ResourceKey.karbon("chat"))
    val SYSTEM by key(ResourceKey.karbon("system"))

    private fun key(resourceKey: ResourceKey): DefaultedRegistryReference<MessageType> =
            RegistryKey(RegistryTypes.MESSAGE_TYPE, resourceKey).asDefaultedReference { Karbon.game.registries }

}