package com.karbonpowered.engine

import java.time.Instant

actual fun dateNow(): String = Instant.now().toString()