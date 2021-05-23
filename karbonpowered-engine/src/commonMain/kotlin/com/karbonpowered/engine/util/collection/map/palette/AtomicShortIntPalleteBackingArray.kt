package com.karbonpowered.engine.util.collection.map.palette

import com.karbonpowered.engine.util.collection.map.AtomicIntShortSingleUseHashMap
import com.karbonpowered.engine.util.roundUpPow2
import kotlinx.atomicfu.AtomicIntArray
import kotlinx.atomicfu.atomic
import kotlin.math.min

class AtomicShortIntPalleteBackingArray(
    size: Int,
    previous: AtomicShortIntBackingArray? = null,
    expand: Boolean = false,
    compress: Boolean = false,
    unique: Int = CALCULATE_UNIQUE
) : AtomicShortIntBackingArray(size) {
    override val width = if (previous == null) {
        1
    } else {
        if (compress) {
            val calculatedUnique = if (unique == CALCULATE_UNIQUE) previous.unique else unique
            roundUpWith(if (expand) unique else unique - 1)
        } else {
            val oldWidth = previous.width
            when {
                oldWidth == 0 -> 1
                oldWidth <= 8 -> oldWidth shl 1
                else -> 16
            }
        }
    }
    private val allowedPalette = getAllowedPalette(size)
    private val paletteSize = min(widthToPaletteSize(width), allowedPalette)
    private val isMaxPaletteSize = allowedPalette == paletteSize
    private val paletteCounter = atomic(0)
    private val idLookup = AtomicIntShortSingleUseHashMap(paletteSize + (paletteSize shr 2))
    private val _palette = AtomicIntArray(paletteSize)
    override val palette: IntArray
        get() = toIntArray(_palette, paletteCounter.value)
    override val backingArray: IntArray
        get() = TODO("Not yet implemented")

    init {
        if (previous == null) {
            paletteCounter.incrementAndGet()
            check(
                idLookup.isEmptyValue(
                    idLookup.putIfAbsent(
                        0,
                        0
                    )
                )
            ) { "Entry was not zero when putting first element into HashMap" }
        } else {
            copyFromPrevious(previous)
        }
    }

    override fun get(index: Int): Int {
        TODO("Not yet implemented")
    }

    override fun set(index: Int, value: Int): Int {
        TODO("Not yet implemented")
    }

    override fun compareAndSet(index: Int, expect: Int, update: Int): Boolean {
        TODO("Not yet implemented")
    }

    companion object {
        private const val CALCULATE_UNIQUE = -1
        private val roundLookup = ByteArray(65537) {
            when (it) {
                1 -> 1
                2 -> 1
                4 -> 2
                8 -> 4
                16 -> 4
                32 -> 8
                64 -> 8
                128 -> 8
                256 -> 8
                512 -> 16
                1024 -> 16
                2048 -> 16
                4096 -> 16
                8192 -> 16
                16384 -> 16
                else -> 0
            }
        }

        fun roundUpWith(i: Int): Int = roundLookup[(i + 1).roundUpPow2()].toInt()

        fun widthToPaletteSize(width: Int) = 1 shl width

        fun getAllowedPalette(length: Int) = length shr 2
    }
}
