package com.karbonpowered.common

import java.nio.ByteBuffer
import java.security.MessageDigest
import java.security.SecureRandom
import kotlin.experimental.and

// This suppression is required to force Kotlin to accept the Java getters as
// val definitions:
//
// - https://youtrack.jetbrains.com/issue/KT-15620
// - https://youtrack.jetbrains.com/issue/KT-27413
@Suppress("NO_ACTUAL_CLASS_MEMBER_FOR_EXPECTED_CLASS")
actual typealias UUID = java.util.UUID

actual val UUID.bytes: ByteArray
    get() = ByteBuffer.allocate(16).putLong(mostSignificantBits).putLong(leastSignificantBits).array()

actual inline val UUID.version: Int
    get() = version()

actual inline val UUID.variant: Int
    get() = variant()

actual fun uuidOf(bytes: ByteArray): UUID {
    require(bytes.size == UUID_BYTES) {
        "Invalid UUID bytes. Expected $UUID_BYTES bytes but found ${bytes.size}"
    }
    var mostSigBits = 0L
    var leastSigBits = 0L
    for (i in 0..7) mostSigBits = mostSigBits shl 8 or (bytes[i] and 0xff.toByte()).toLong()
    for (i in 8..15) leastSigBits = leastSigBits shl 8 or (bytes[i] and 0xff.toByte()).toLong()
    return UUID(mostSigBits, leastSigBits)
}

actual fun uuidFrom(charSequence: CharSequence): UUID {
    require(charSequence.length == UUID_STRING_LENGTH) {
        "Invalid UUID string, expected exactly 36 characters but got ${charSequence.length}: $charSequence"
    }
    require(charSequence[8] == '-' && charSequence[13] == '-' && charSequence[18] == '-' && charSequence[23] == '-') {
        "Invalid UUID string: $charSequence"
    }
    var mostSignificantBits = charSequence.hexValue(0) shl 60
    mostSignificantBits = mostSignificantBits or (charSequence.hexValue(1) shl 56)
    mostSignificantBits = mostSignificantBits or (charSequence.hexValue(2) shl 52)
    mostSignificantBits = mostSignificantBits or (charSequence.hexValue(3) shl 48)
    mostSignificantBits = mostSignificantBits or (charSequence.hexValue(4) shl 44)
    mostSignificantBits = mostSignificantBits or (charSequence.hexValue(5) shl 40)
    mostSignificantBits = mostSignificantBits or (charSequence.hexValue(6) shl 36)
    mostSignificantBits = mostSignificantBits or (charSequence.hexValue(7) shl 32)

    mostSignificantBits = mostSignificantBits or (charSequence.hexValue(9) shl 28)
    mostSignificantBits = mostSignificantBits or (charSequence.hexValue(10) shl 24)
    mostSignificantBits = mostSignificantBits or (charSequence.hexValue(11) shl 20)
    mostSignificantBits = mostSignificantBits or (charSequence.hexValue(12) shl 16)

    mostSignificantBits = mostSignificantBits or (charSequence.hexValue(14) shl 12)
    mostSignificantBits = mostSignificantBits or (charSequence.hexValue(15) shl 8)
    mostSignificantBits = mostSignificantBits or (charSequence.hexValue(16) shl 4)
    mostSignificantBits = mostSignificantBits or (charSequence.hexValue(17))

    var leastSignificantBits = charSequence.hexValue(19) shl 60
    leastSignificantBits = leastSignificantBits or (charSequence.hexValue(20) shl 56)
    leastSignificantBits = leastSignificantBits or (charSequence.hexValue(21) shl 52)
    leastSignificantBits = leastSignificantBits or (charSequence.hexValue(22) shl 48)

    leastSignificantBits = leastSignificantBits or (charSequence.hexValue(24) shl 44)
    leastSignificantBits = leastSignificantBits or (charSequence.hexValue(25) shl 40)
    leastSignificantBits = leastSignificantBits or (charSequence.hexValue(26) shl 36)
    leastSignificantBits = leastSignificantBits or (charSequence.hexValue(27) shl 32)
    leastSignificantBits = leastSignificantBits or (charSequence.hexValue(28) shl 28)
    leastSignificantBits = leastSignificantBits or (charSequence.hexValue(29) shl 24)
    leastSignificantBits = leastSignificantBits or (charSequence.hexValue(30) shl 20)
    leastSignificantBits = leastSignificantBits or (charSequence.hexValue(31) shl 16)
    leastSignificantBits = leastSignificantBits or (charSequence.hexValue(32) shl 12)
    leastSignificantBits = leastSignificantBits or (charSequence.hexValue(33) shl 8)
    leastSignificantBits = leastSignificantBits or (charSequence.hexValue(34) shl 4)
    leastSignificantBits = leastSignificantBits or (charSequence.hexValue(35))

    return UUID(mostSignificantBits, leastSignificantBits)
}

private fun CharSequence.hexValue(index: Int): Long {
    val digit = this[index]
    var value: Long = -1
    require(digit.toInt() >= 0 && digit.toInt() < UUID_HEX_VALUES.size && UUID_HEX_VALUES[digit.toInt()].let {
        value = it
        value
    } != -1L) {
        "Invalid UUID string, encountered non-hexadecimal digit `$digit` at index $index in: $this"
    }
    return value
}

actual object UUID3Hasher : UUIDHasher {
    private val digest = MessageDigest.getInstance("MD5")
    override val version: Int = 3

    override fun update(input: ByteArray) = digest.update(input)

    override fun digest(): ByteArray = digest.digest()
}

actual object UUID5Hasher : UUIDHasher {
    private val digest = MessageDigest.getInstance("SHA-1")
    override val version: Int = 5

    override fun update(input: ByteArray) = digest.update(input)

    override fun digest(): ByteArray = digest.digest()
}

private val secureRandom = SecureRandom()

internal actual fun randomUuidBytes(): ByteArray = ByteArray(UUID_BYTES).also {
    secureRandom.nextBytes(it)
}