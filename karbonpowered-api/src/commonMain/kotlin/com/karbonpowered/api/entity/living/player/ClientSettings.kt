package com.karbonpowered.api.entity.living.player

interface ClientSettings {
    val locale: String
    val viewDistance: Int
    val chatVisibility: ChatVisibility
    val hasChatColors: Boolean
    val skinParts: Set<SkinPart>
    val mainHand: HandType
}