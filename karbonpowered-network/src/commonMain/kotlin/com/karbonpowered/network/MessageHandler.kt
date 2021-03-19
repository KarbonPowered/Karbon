package com.karbonpowered.network

interface MessageHandler<S : Session, T : Message> {
    fun handle(session: S, message: T)
}