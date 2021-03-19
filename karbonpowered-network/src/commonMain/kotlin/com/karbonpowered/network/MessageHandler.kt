package com.karbonpowered.network

interface MessageHandler<S : Session, T : Message> {
    suspend fun handle(session: S, message: T)
}