package com.karbonpowered.common

import java.security.MessageDigest

private val md5 = MessageDigest.getInstance("MD5")

actual fun ByteArray.md5(): ByteArray = md5.digest(this)
