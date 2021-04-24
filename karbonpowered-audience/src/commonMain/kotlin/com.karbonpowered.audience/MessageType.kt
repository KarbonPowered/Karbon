package com.karbonpowered.audience

interface MessageType

object MessageTypes {
    val CHAT = object : MessageType {}
    val SYSTEM = object : MessageType {}
    val ACTION_BAR = object : MessageType {}
}