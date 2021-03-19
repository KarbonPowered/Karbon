package com.karbonpowered.network.service

import com.karbonpowered.network.Message
import com.karbonpowered.network.MessageHandler
import com.karbonpowered.network.Session
import kotlin.reflect.KClass

class HandlerLookupService {
    private val handlerMap = mutableMapOf<KClass<out Message>, MessageHandler<*, *>>()

    fun <M : Message, H : MessageHandler<*, out M>> bind(clazz: KClass<M>, handler: H) {
        handlerMap[clazz] = handler
    }

    operator fun <M : Message> get(clazz: KClass<out M>): MessageHandler<Session, M>? =
        handlerMap[clazz] as? MessageHandler<Session, M>

    override fun toString(): String = "HandlerLookupService(handlers=$handlerMap)"
}