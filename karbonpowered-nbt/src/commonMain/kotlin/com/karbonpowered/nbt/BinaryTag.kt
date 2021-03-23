@file:Suppress("NOTHING_TO_INLINE")

package com.karbonpowered.nbt

import com.karbonpowered.common.UUID
import com.karbonpowered.io.Codec
import io.ktor.utils.io.core.*
import kotlin.reflect.KClass

sealed class BinaryTag

interface BinaryTagCodec<T : BinaryTag> : Codec<T> {
    val opcode: Byte
}

private val KClass<out BinaryTag>.binaryTagCodec: BinaryTagCodec<out BinaryTag> get() = when (this) {
    EndBinaryTag::class -> EndBinaryTag
    ByteBinaryTag::class -> ByteBinaryTag
    ShortBinaryTag::class -> ShortBinaryTag
    IntBinaryTag::class -> IntBinaryTag
    LongBinaryTag::class -> LongBinaryTag
    DoubleBinaryTag::class -> DoubleBinaryTag
    ByteArrayBinaryTag::class -> ByteArrayBinaryTag
    StringBinaryTag::class -> StringBinaryTag
    ListBinaryTag::class -> ListBinaryTag
    CompoundBinaryTag::class -> CompoundBinaryTag
    IntArrayBinaryTag::class -> IntArrayBinaryTag
    LongArrayBinaryTag::class -> LongArrayBinaryTag
    else -> throw IllegalArgumentException("Unknown binary tag $this")
}

private val Byte.binaryTagType: KClass<out BinaryTag> get() = when(this) {
    EndBinaryTag.opcode -> EndBinaryTag::class
    ByteBinaryTag.opcode -> ByteBinaryTag::class
    ShortBinaryTag.opcode -> ShortBinaryTag::class
    IntBinaryTag.opcode -> IntBinaryTag::class
    LongBinaryTag.opcode -> LongBinaryTag::class
    DoubleBinaryTag.opcode -> DoubleBinaryTag::class
    ByteArrayBinaryTag.opcode -> ByteArrayBinaryTag::class
    StringBinaryTag.opcode -> StringBinaryTag::class
    ListBinaryTag.opcode -> ListBinaryTag::class
    CompoundBinaryTag.opcode -> CompoundBinaryTag::class
    IntArrayBinaryTag.opcode -> IntArrayBinaryTag::class
    LongArrayBinaryTag.opcode -> LongArrayBinaryTag::class
    else -> throw IllegalArgumentException("Unknown binary tag $this")
}

object EndBinaryTag : BinaryTag(), BinaryTagCodec<EndBinaryTag> {
    override val opcode: Byte = 0

    override suspend fun encode(output: Output, data: EndBinaryTag) {
    }

    override suspend fun decode(input: Input): EndBinaryTag = EndBinaryTag
}

abstract class NumberBinaryTag : BinaryTag() {
    abstract val value: Number
    fun toBoolean(): Boolean = value == 1
    fun toByte(): Byte = value.toByte()
    fun toShort(): Short = value.toShort()
    fun toInt(): Int = value.toInt()
    fun toLong(): Long = value.toLong()
    fun toFloat(): Float = value.toFloat()
    fun toDouble(): Double = value.toDouble()
}

data class ByteBinaryTag(
    override val value: Byte
) : NumberBinaryTag() {
    constructor(value: Boolean) : this(if (value) 1 else 0)

    companion object : BinaryTagCodec<ByteBinaryTag> {
        override val opcode: Byte = 1

        override suspend fun encode(output: Output, data: ByteBinaryTag) {
            output.writeByte(data.toByte())
        }

        override suspend fun decode(input: Input): ByteBinaryTag {
            return ByteBinaryTag(input.readByte())
        }
    }
}

inline fun Boolean.toBinaryTag(): ByteBinaryTag = ByteBinaryTag(this)
inline fun Byte.toBinaryTag(): ByteBinaryTag = ByteBinaryTag(this)

data class ShortBinaryTag(
    override val value: Short
) : NumberBinaryTag() {
    companion object : BinaryTagCodec<ShortBinaryTag> {
        override val opcode: Byte = 2

        override suspend fun encode(output: Output, data: ShortBinaryTag) {
            output.writeShort(data.value)
        }

        override suspend fun decode(input: Input): ShortBinaryTag {
            return ShortBinaryTag(input.readShort())
        }
    }
}

inline fun Short.toBinaryTag(): ShortBinaryTag = ShortBinaryTag(this)

data class IntBinaryTag(
    override val value: Int
) : NumberBinaryTag() {
    companion object : BinaryTagCodec<IntBinaryTag> {
        override val opcode: Byte = 3

        override suspend fun encode(output: Output, data: IntBinaryTag) {
            output.writeInt(data.value)
        }

        override suspend fun decode(input: Input): IntBinaryTag {
            return IntBinaryTag(input.readInt())
        }
    }
}

inline fun Int.toBinaryTag(): IntBinaryTag = IntBinaryTag(this)

data class LongBinaryTag(
    override val value: Long
) : NumberBinaryTag() {
    companion object : BinaryTagCodec<LongBinaryTag> {
        override val opcode: Byte = 4

        override suspend fun encode(output: Output, data: LongBinaryTag) {
            output.writeLong(data.value)
        }

        override suspend fun decode(input: Input): LongBinaryTag {
            return LongBinaryTag(input.readLong())
        }
    }
}

inline fun Long.toBinaryTag(): LongBinaryTag = LongBinaryTag(this)

data class FloatBinaryTag(
    override val value: Float
) : NumberBinaryTag() {
    companion object : BinaryTagCodec<FloatBinaryTag> {
        override val opcode: Byte = 5

        override suspend fun encode(output: Output, data: FloatBinaryTag) {
            output.writeFloat(data.value)
        }

        override suspend fun decode(input: Input): FloatBinaryTag {
            return FloatBinaryTag(input.readFloat())
        }
    }
}

inline fun Float.toBinaryTag(): FloatBinaryTag = FloatBinaryTag(this)

data class DoubleBinaryTag(
    override val value: Double
) : NumberBinaryTag() {
    companion object : BinaryTagCodec<DoubleBinaryTag> {
        override val opcode: Byte = 6

        override suspend fun encode(output: Output, data: DoubleBinaryTag) {
            output.writeDouble(data.value)
        }

        override suspend fun decode(input: Input): DoubleBinaryTag {
            return DoubleBinaryTag(input.readDouble())
        }
    }
}

inline fun Double.toBinaryTag(): DoubleBinaryTag = DoubleBinaryTag(this)

data class StringBinaryTag(
    val value: String
) : BinaryTag() {
    companion object : BinaryTagCodec<StringBinaryTag> {
        override val opcode: Byte = 8

        override suspend fun encode(output: Output, data: StringBinaryTag) {
            output.writeText(data.value)
        }

        override suspend fun decode(input: Input): StringBinaryTag {
            return StringBinaryTag(input.readText())
        }
    }
}

inline fun String.toBinaryTag(): StringBinaryTag = StringBinaryTag(this)

data class CompoundBinaryTag(
    private val tags: Map<String, BinaryTag>
) : BinaryTag(), Map<String, BinaryTag> by tags {
    constructor(vararg entries: Pair<String, BinaryTag>) : this(entries.toMap())

    companion object : BinaryTagCodec<CompoundBinaryTag> {
        override val opcode: Byte = 10

        @Suppress("UNCHECKED_CAST")
        override suspend fun encode(output: Output, data: CompoundBinaryTag) {
            data.forEach { (key, value) ->
                val codec = value::class.binaryTagCodec as BinaryTagCodec<BinaryTag>
                if (value != EndBinaryTag) {
                    output.writeByte(codec.opcode)
                    output.writeText(key)
                    codec.encode(output, value)
                }
            }
            output.writeByte(EndBinaryTag.opcode)
        }

        override suspend fun decode(input: Input): CompoundBinaryTag {
            val tags = mutableMapOf<String,BinaryTag>()
            while (true) {
                val type = input.readByte().binaryTagType
                if (type == EndBinaryTag::class) break
                val key = input.readText()
                val tag = type.binaryTagCodec.decode(input)
                tags[key] = tag
            }
            return CompoundBinaryTag(tags)
        }
    }
}

data class ListBinaryTag<T : BinaryTag>(
    val valueType: KClass<T>,
    private val tags: List<T>
) : BinaryTag(), List<T> by tags {
    @Suppress("UNCHECKED_CAST")
    companion object : BinaryTagCodec<ListBinaryTag<out BinaryTag>> {
        override val opcode: Byte = 9

        override suspend fun encode(output: Output, data: ListBinaryTag<out BinaryTag>) {
            val codec = data.valueType.binaryTagCodec as BinaryTagCodec<BinaryTag>
            output.writeByte(codec.opcode)
            output.writeInt(data.size)
            data.forEach {
                codec.encode(output, it)
            }
        }

        override suspend fun decode(input: Input): ListBinaryTag<out BinaryTag> {
            val type = input.readByte().binaryTagType as KClass<BinaryTag>
            val codec = type.binaryTagCodec
            val tags = List(input.readInt()) {
                codec.decode(input)
            }
            return ListBinaryTag(type, tags)
        }
    }
}

inline fun <reified T : BinaryTag> ListBinaryTag(tags: List<T>): ListBinaryTag<T> = ListBinaryTag(T::class, tags)
inline fun Iterable<Boolean>.toBinaryTag() = ListBinaryTag(this.map { it.toBinaryTag() })
inline fun Iterable<Byte>.toBinaryTag() = ListBinaryTag(this.map { it.toBinaryTag() })
inline fun Iterable<Short>.toBinaryTag() = ListBinaryTag(this.map { it.toBinaryTag() })
inline fun Iterable<Float>.toBinaryTag() = ListBinaryTag(this.map { it.toBinaryTag() })
inline fun Iterable<Double>.toBinaryTag() = ListBinaryTag(this.map { it.toBinaryTag() })
inline fun Iterable<String>.toBinaryTag() = ListBinaryTag(this.map { it.toBinaryTag() })
inline fun Iterable<BinaryTag>.toBinaryTag() = ListBinaryTag(this.toList())

data class ByteArrayBinaryTag(
    val value: ByteArray
) : BinaryTag(), Collection<Byte> {
    override val size: Int get() = value.size
    override fun contains(element: Byte): Boolean = value.contains(element)
    override fun containsAll(elements: Collection<Byte>): Boolean = elements.all { contains(it) }
    override fun isEmpty(): Boolean = value.isEmpty()
    override fun iterator(): Iterator<Byte> = value.iterator()

    operator fun get(index: Int): Byte = value[index]

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ByteArrayBinaryTag

        if (!value.contentEquals(other.value)) return false

        return true
    }

    override fun hashCode(): Int = value.contentHashCode()

    companion object : BinaryTagCodec<ByteArrayBinaryTag> {
        override val opcode: Byte = 7

        override suspend fun encode(output: Output, data: ByteArrayBinaryTag) {
            output.writeInt(data.size)
            output.writeFully(data.value)
        }

        override suspend fun decode(input: Input): ByteArrayBinaryTag {
            return ByteArrayBinaryTag(ByteArray(input.readInt()) {
                input.readByte()
            })
        }
    }
}

inline fun ByteArray.toBinaryTag(): ByteArrayBinaryTag = ByteArrayBinaryTag(this)

data class IntArrayBinaryTag(
    val value: IntArray
) : BinaryTag(), Collection<Int> {
    constructor(vararg value: Int) : this(value)

    constructor(value: UUID) : this(
        (value.mostSignificantBits shr 32).toInt(),
        value.mostSignificantBits.toInt(),
        (value.leastSignificantBits shr 32).toInt(),
        value.leastSignificantBits.toInt()
    )

    override val size: Int get() = value.size
    override fun contains(element: Int): Boolean = value.contains(element)
    override fun containsAll(elements: Collection<Int>): Boolean = elements.all { contains(it) }
    override fun isEmpty(): Boolean = value.isEmpty()
    override fun iterator(): Iterator<Int> = value.iterator()

    operator fun get(index: Int): Int = value[index]

    fun toUniqueId(): UUID {
        return UUID(
            value[0].toLong() shl 32 or value[1].toLong() and 0xFFFFFFFF,
            value[2].toLong() shl 32 or value[3].toLong() and 0xFFFFFFFF
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as IntArrayBinaryTag

        if (!value.contentEquals(other.value)) return false

        return true
    }

    override fun hashCode(): Int = value.contentHashCode()

    companion object : BinaryTagCodec<IntArrayBinaryTag> {
        override val opcode: Byte = 11

        override suspend fun encode(output: Output, data: IntArrayBinaryTag) {
            output.writeInt(data.size)
            output.writeFully(data.value)
        }

        override suspend fun decode(input: Input): IntArrayBinaryTag {
            return IntArrayBinaryTag(IntArray(input.readInt()) {
                input.readInt()
            })
        }
    }
}

fun IntArray.toBinaryTag(): IntArrayBinaryTag = IntArrayBinaryTag(this)

data class LongArrayBinaryTag(
    val value: LongArray
) : BinaryTag(), Collection<Long> {
    constructor(vararg value: Long) : this(value)

    override val size: Int get() = value.size
    override fun contains(element: Long): Boolean = value.contains(element)
    override fun containsAll(elements: Collection<Long>): Boolean = elements.all { contains(it) }
    override fun isEmpty(): Boolean = value.isEmpty()
    override fun iterator(): Iterator<Long> = value.iterator()

    operator fun get(index: Int): Long = value[index]

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as LongArrayBinaryTag

        if (!value.contentEquals(other.value)) return false

        return true
    }

    override fun hashCode(): Int = value.contentHashCode()

    companion object : BinaryTagCodec<LongArrayBinaryTag> {
        override val opcode: Byte = 12

        override suspend fun encode(output: Output, data: LongArrayBinaryTag) {
            output.writeInt(data.size)
            output.writeFully(data.value)
        }

        override suspend fun decode(input: Input): LongArrayBinaryTag {
            return LongArrayBinaryTag(LongArray(input.readInt()) {
                input.readLong()
            })
        }
    }
}

fun LongArray.toBinaryTag(): LongArrayBinaryTag = LongArrayBinaryTag(this)