package com.karbonpowered.io

import io.ktor.utils.io.core.*
import io.ktor.utils.io.core.internal.*


@OptIn(DangerousInternalIoApi::class)
fun createBuffer(size: Int, block: Buffer.() -> Unit): Buffer = withBuffer(size) {
    block(this)
    this
}
