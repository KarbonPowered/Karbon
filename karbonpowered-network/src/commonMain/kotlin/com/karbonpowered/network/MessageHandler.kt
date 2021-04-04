package com.karbonpowered.network

fun interface MessageHandler<S : Session, T : Message> {
    fun handle(session: S, message: T)
}