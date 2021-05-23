package com.karbonpowered.engine.util.collection.map

import com.karbonpowered.engine.util.roundUpPow2
import kotlinx.atomicfu.AtomicIntArray

/**
 * This class implements a variable width Atomic array.
 * It is backed by an AtomicInt array.
 *
 * Entries widths can be a power of 2 from 1 to 32
 */
class AtomicVariableWidthArray(
    val length: Int,
    val width: Int,
    val initial: IntArray? = null
) {
    init {
        require(width.roundUpPow2() == width && width >= 1 && width <= 32) { "Width must be a power of 2 between 1 and 32 $width" }
    }

    private val valuesPerInt = 32 / width
    private val newLength = length / valuesPerInt

    init {
        require(newLength * valuesPerInt == length) { "The length must be a multiple of $valuesPerInt for arrays of width $width" }
        if (initial != null) {
            require(newLength == initial.size) { "Length of packed array did not match expected" }
        }
    }

    private val indexShift = 5 - log2[width]
    private val subIndexMask = (1 shl indexShift) - 1
    private val valueShift = IntArray(valuesPerInt) {
        it * width
    }
    private val valueBitmask = IntArray(valuesPerInt) {
        ((1 shl width) - 1) shl valueShift[it]
    }
    private val array = if (initial != null) {
        AtomicIntArray(initial.size).apply {
            repeat(initial.size) {
                get(it).value = initial[it]
            }
        }
    } else {
        AtomicIntArray(newLength)
    }
    private val isFullWidth = width == 32
    val maxValue = if (isFullWidth) -1 else valueBitmask[0]

    operator fun get(i: Int): Int = if (isFullWidth) {
        array[i].value
    } else {
        unpack(array[i.index()].value, i.subIndex())
    }

    operator fun set(i: Int, value: Int) {
        if (isFullWidth) {
            array[i].value = value
        }

        var success = false
        val index = i.index()
        val subIndex = i.subIndex()
        while (!success) {
            val prev = array[index].value
            val next = pack(prev, value, subIndex)
            success = array[index].compareAndSet(prev, next)
        }
    }

    fun compareAndSet(i: Int, expect: Int, update: Int): Boolean {
        if (isFullWidth) {
            return array[i].compareAndSet(expect, update)
        }

        val index = i.index()
        val subIndex = i.subIndex()
        var success = false
        while (!success) {
            val prev = array[index].value
            if (unpack(prev, subIndex) != expect) {
                return false
            }

            val next = pack(prev, update, subIndex)
            success = array[index].compareAndSet(prev, next)
        }
        return true
    }

    fun getAndSet(i: Int, newValue: Int): Int {
        if (isFullWidth) {
            return array[i].getAndSet(newValue)
        }

        val index = i.index()
        val subIndex = i.subIndex()
        var prev = 0
        var success = false
        while (!success) {
            prev = array[index].value
            val next = pack(prev, newValue, subIndex)
            success = array[index].compareAndSet(prev, next)
        }
        return unpack(prev, subIndex)
    }

    fun addAndGet(i: Int, delta: Int, old: Boolean = false): Int {
        if (isFullWidth) {
            return if (old) {
                array[i].getAndAdd(delta)
            } else {
                array[i].addAndGet(delta)
            }
        }
        val index = i.index()
        val subIndex = i.subIndex()
        var prev: Int
        var prevValue = 0
        var newValue = 0
        var success = false
        while (!success) {
            prev = array[index].value
            prevValue = unpack(prev, subIndex)
            newValue = prevValue + delta
            val next = pack(prev, newValue, subIndex)
            success = array[index].compareAndSet(prev, next)
        }
        return (if (old) prevValue else newValue) and valueBitmask[0]
    }

    fun getAndAdd(i: Int, delta: Int) = addAndGet(i, delta, true)

    fun incrementAndGet(i: Int) = addAndGet(i, 1)
    fun decrementAndGet(i: Int) = addAndGet(i, -1)
    fun getAndIncrement(i: Int) = addAndGet(i, 1, true)
    fun getAndDecrement(i: Int) = addAndGet(i, -1, true)

    private fun Int.index() = this shr indexShift
    private fun Int.subIndex() = subIndexMask and this

    private fun unpack(packed: Int, subIndex: Int): Int = packed and valueBitmask[subIndex] ushr valueShift[subIndex]

    private fun pack(prev: Int, newValue: Int, subIndex: Int): Int {
        val bitmask = valueBitmask[subIndex]
        val shift = valueShift[subIndex]
        return (prev and bitmask.inv()) or (bitmask and (newValue shl shift))
    }

    companion object {
        val log2 = IntArray(33) {
            when (it) {
                2 -> 1
                4 -> 2
                8 -> 3
                16 -> 4
                32 -> 5
                else -> 0
            }
        }
    }
}
