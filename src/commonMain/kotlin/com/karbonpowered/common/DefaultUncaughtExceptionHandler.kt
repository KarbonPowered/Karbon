package com.karbonpowered.common

import com.karbonpowered.log.Logger
import io.ktor.utils.io.errors.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.channels.ClosedSendChannelException
import kotlin.coroutines.CoroutineContext

/**
 * Handles all uncaught exceptions and logs errors with the specified [logger]
 * ignoring [CancellationException] and [IOException].
 */
class DefaultUncaughtExceptionHandler(
    private val logger: () -> Logger
) : CoroutineExceptionHandler {
    constructor(logger: Logger) : this({ logger })

    override val key: CoroutineContext.Key<*>
        get() = CoroutineExceptionHandler.Key

    override fun handleException(context: CoroutineContext, exception: Throwable) {
        if (exception is CancellationException) return
        if (exception is ClosedReceiveChannelException) return
        if (exception is ClosedSendChannelException) return
        if (exception is IOException) return

        val coroutineName = context[CoroutineName] ?: context.toString()

        logger().error("Unhandled exception caught for $coroutineName", exception)
    }
}