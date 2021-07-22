@file:Suppress("NOTHING_TO_INLINE")

package com.karbonpowered.common

import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.native.concurrent.SharedImmutable

// Number of bytes in a UUID
internal const val UUID_BYTES = 16

// Number of characters in a UUID string
internal const val UUID_STRING_LENGTH = 36

// Ranges of non-hyphen characters in a UUID string
@SharedImmutable
internal val UUID_CHAR_RANGES: List<IntRange> = listOf(
    0 until 8,
    9 until 13,
    14 until 18,
    19 until 23,
    24 until 36
)

internal val UUID_HEX_VALUES = LongArray(128) {
    when (it.toChar()) {
        '0' -> 0L
        '1' -> 1L
        '2' -> 2L
        '3' -> 3L
        '4' -> 4L
        '5' -> 5L
        '6' -> 6L
        '7' -> 7L
        '8' -> 8L
        '9' -> 9L
        'a', 'A' -> 10L
        'b', 'B' -> 11L
        'c', 'C' -> 12L
        'd', 'D' -> 13L
        'e', 'E' -> 14L
        'f', 'F' -> 15L
        else -> -1L
    }
}

// Indices of the hyphen characters in a UUID string
@SharedImmutable
internal val UUID_HYPHEN_INDICES = listOf(8, 13, 18, 23)

// UUID chars arranged from smallest to largest, so they can be indexed by their byte representations
@SharedImmutable
internal val UUID_CHARS = ('0'..'9') + ('a'..'f')

/**
 * A RFC4122 UUID
 *
 * @param mostSignificantBits The 64 most significant bits of the [UUID].
 * @param leastSignificantBits The 64 least significant bits of the [UUID].
 */
expect class UUID(mostSignificantBits: Long, leastSignificantBits: Long) : Comparable<UUID> {
    /** The most significant 64 bits of this UUID's 128 bit value. */
    val mostSignificantBits: Long

    /** The least significant 64 bits of this UUID's 128 bit value. */
    val leastSignificantBits: Long
}

/** Gets the raw UUID bytes */
expect val UUID.bytes: ByteArray

/**
 * The variant of the [UUID], determines the interpretation of the bits.
 *
 * - **`0`** – special case for the Nil UUID as well as reserved for NCS
 * - **`2`** – Leach-Salz, as defined in [RFC 4122](https://tools.ietf.org/html/rfc4122) and used by this class
 * - **`6`** – reserved for Microsoft (GUID) backwards compatibility
 * - **`7`** – reserved for future extension
 *
 * @return The variant number of this [UUID].
 * @see <a href="https://tools.ietf.org/html/rfc4122#section-4.1.1">RFC 4122: Section 4.1.1</a>
 */
expect val UUID.variant: Int

/**
 * The version of the [Uuid], denoting the generating algorithm.
 *
 * - **`0`** – special case for the Nil UUID
 * - **`1`** – time-based
 * - **`2`** – DCE security
 * - **`3`** – name-based using MD5 hashing
 * - **`4`** – random or pseudo-random
 * - **`5`** – name-based using SHA-1 hashing
 * - **`6`–`15`** – reserved for future extension
 *
 * Note that the version returned by this function is only meaningful if the [UUID.variant] is
 * [RFC 4122](https://tools.ietf.org/html/rfc4122).
 *
 * @return The version number of this [UUID].
 * @see <a href="https://tools.ietf.org/html/rfc4122#section-4.1.3">RFC 4122: Section 4.1.3</a>
 */
expect val UUID.version: Int

/**
 * Construct new [UUID] instance from a [String]
 *
 * @param from The [String] representation of the UUID
 */
expect fun uuidFrom(charSequence: CharSequence): UUID

/**
 * Constructs a new [UUID] from the given [bytes]
 * @throws IllegalArgumentException, if bytes.count() is not 16
 */
expect fun uuidOf(bytes: ByteArray): UUID

/**
 * Construct new version 4 [UUID].
 *
 * Version 4 UUIDs are randomly generated from the best available random source.
 * The selection of that source is depends on the platform. Some systems may be
 * bad at generating sufficient entropy, e.g. virtual machines, and this might
 * lead to collisions faster than desired. Version 5 may be used if this is the
 * case and no other measures are possible to increase the entropy of the random
 * source of the platform.
 *
 * @return New version 4 [UUID] of random data.
 * @see <a href="https://tools.ietf.org/html/rfc4122#section-4.4">RFC 4122: Section 4.4</a>
 */
fun uuid4(): UUID = uuidOf(randomUuidBytes().setVersion(4))

/**
 * Interface for computing a hash for a "Name-Based" UUID
 */
interface UUIDHasher {

    /**
     * The UUID version, for which this
     * hash algorithm is being used:
     * - 3 for MD5
     * - 5 for SHA-1
     */
    val version: Int

    /**
     * Updates the hash's digest with more bytes
     * @param input to update the hasher's digest
     */
    fun update(input: ByteArray)

    /**
     * Completes the hash computation and returns the result
     * @note The hasher should not be used after this call
     */
    fun digest(): ByteArray
}

expect object UUID3Hasher : UUIDHasher

expect object UUID5Hasher : UUIDHasher

inline fun uuid3Of(namespace: UUID, name: String): UUID = nameBasedUuidOf(namespace, name, UUID3Hasher)

inline fun uuid5Of(namespace: UUID, name: String): UUID = nameBasedUuidOf(namespace, name, UUID5Hasher)

fun nameBasedUuidOf(namespace: UUID, name: String, hasher: UUIDHasher): UUID {
    hasher.update(namespace.bytes)
    hasher.update(name.encodeToByteArray())
    val hashedBytes = hasher.digest()
    hashedBytes[6] = hashedBytes[6]
        .and(0b00001111) // clear the 4 most sig bits
        .or(hasher.version.shl(4).toByte())
    hashedBytes[8] = hashedBytes[8]
        .and(0b00111111) // clear the 2 most sig bits
        .or(-0b10000000) // set 2 most sig to 10
    return uuidOf(hashedBytes.copyOf(UUID_BYTES))
}

/**
 * Set the [UUID.version] on this big-endian [ByteArray]. The [UUID.variant] is
 * always set to the RFC 4122 one since this is the only variant supported by
 * the [UUID] implementation.
 *
 * @return Itself after setting the [UUID.variant] and [UUID.version].
 */
@Suppress("NOTHING_TO_INLINE")
internal inline fun ByteArray.setVersion(version: Int): ByteArray = apply {
    this[6] = this[6] and 0x0f /* clear version        */
    this[6] = this[6] or (version shl 4).toByte() /* set version 4 */
    this[8] = this[8] and 0x3f /* clear variant */
    this[8] = this[8] or 0x80.toByte() /* set to IETF variant  */
}

internal expect fun randomUuidBytes(): ByteArray