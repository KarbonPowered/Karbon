package com.karbonpowered.engine.util

import kotlin.math.ceil

class VariableValueArray(
        val bitsPerValue: Int,
        val capacity: Int
) : Iterable<Int> {
    init {
        require(capacity >= 0) {
            "capacity ($capacity) must not be negative"
        }
        require(bitsPerValue >= 1) {
            "bitsPerValue ($bitsPerValue) must not be less than 1"
        }
        require(bitsPerValue <= 64) {
            "bitsPerValue ($bitsPerValue) must not be greater than 64"
        }
    }

    val backing = LongArray(ceil(bitsPerValue * capacity / 64.0).toInt())
    val valueMask = (1L shl bitsPerValue) - 1L

    fun fill(data: LongArray) {
        data.copyInto(backing, endIndex = backing.size)
    }

    operator fun get(index: Int): Int {
        var index = index
        checkIndex(index)
        index *= bitsPerValue
        var i0 = index shr 6
        val i1 = index and 0x3f
        var value = backing[i0] ushr i1
        val i2 = i1 + bitsPerValue
        // The value is divided over two long values
        if (i2 > 64) {
            value = value or (backing[++i0] shl 64 - i1)
        }
        return (value and valueMask).toInt()
    }

    operator fun set(index: Int, value: Int) {
        var index = index
        checkIndex(index)
        if (value < 0) {
            throw IllegalArgumentException("value ($value) must not be negative")
        }
        if (value > valueMask) {
            throw IllegalArgumentException("value ($value) must not be greater than $valueMask")
        }
        index *= bitsPerValue
        var i0 = index shr 6
        val i1 = index and 0x3f
        backing[i0] = backing[i0] and (valueMask shl i1).inv() or (value and valueMask.toInt()).toLong() shl i1
        val i2 = i1 + bitsPerValue
        // The value is divided over two long values
        if (i2 > 64) {
            i0++
            backing[i0] = backing[i0] and ((1L shl i2 - 64) - 1L).inv() or value.toLong() shr 64 - i1
        }
    }

    override fun iterator(): Iterator<Int> = object : Iterator<Int> {
        var index = 0
        override fun hasNext(): Boolean = index < capacity

        override fun next(): Int = get(index++)
    }

    private fun checkIndex(index: Int) {
        if (index < 0) {
            throw IndexOutOfBoundsException("index ($index) must not be negative")
        }
        if (index >= capacity) {
            throw IndexOutOfBoundsException("index ($index) must not be greater than the capacity ($capacity)")
        }
    }

    fun increaseBitsPerValueTo(newBitsPerValue: Int): VariableValueArray {
        require(newBitsPerValue >= bitsPerValue) {
            "Cannot decrease bits per value! (was $bitsPerValue, new size $newBitsPerValue)"
        }
        return if (newBitsPerValue == bitsPerValue) {
            copy()
        } else {
            VariableValueArray(newBitsPerValue, capacity).apply {
                repeat(capacity) {
                    set(it, this@VariableValueArray[it])
                }
            }
        }
    }

    fun copy(): VariableValueArray {
        val copy = VariableValueArray(bitsPerValue, capacity)
        backing.copyInto(copy.backing)
        return copy
    }

    companion object {
        fun calculateNeededBits(number: Int): Int {
            var n = number
            var count = 0
            do {
                count++
                n = n ushr 1
            } while (n != 0)
            return count
        }
    }
}