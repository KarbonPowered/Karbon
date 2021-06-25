package com.karbonpowered.core.entity.living.player

import com.karbonpowered.api.entity.living.player.ChatVisibility
import com.karbonpowered.api.entity.living.player.ClientSettings
import com.karbonpowered.api.entity.living.player.HandType
import com.karbonpowered.api.entity.living.player.SkinPart

data class ClientSettingsImpl(
    override val locale: String,
    override val viewDistance: Int,
    override val chatVisibility: ChatVisibility,
    override val hasChatColors: Boolean,
    override val skinParts: Set<SkinPart>,
    override val mainHand: HandType,
    override val textFiltering: Boolean
) : ClientSettings