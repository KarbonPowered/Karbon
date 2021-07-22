package com.karbonpowered.engine.util.collection.map.palette.backing

import com.karbonpowered.engine.util.collection.map.AtomicIntShortSingleUseHashMap
import com.karbonpowered.engine.util.collection.map.AtomicVariableWidthArray
import com.karbonpowered.engine.util.collection.map.palette.backing.AtomicShortIntPaletteBackingArray.Companion.allowedPalette
import com.karbonpowered.engine.util.collection.map.palette.backing.AtomicShortIntPaletteBackingArray.Companion.roundUpWith
import com.karbonpowered.engine.util.collection.map.palette.exception.PaletteFullException
import com.karbonpowered.engine.util.roundUpPow2
import kotlinx.atomicfu.AtomicInt
import kotlinx.atomicfu.AtomicIntArray
import kotlinx.atomicfu.atomic
import kotlin.math.min

private const val CALCULATE_UNIQUE = -1

private fun widthToPaletteSize(width: Int) = 1 shl width

class AtomicShortIntPaletteBackingArray internal constructor(
    size: Int,
    previous: AbstractAtomicShortIntBackingArray?,
    override val width: Int,
    override val paletteSize: Int,
    private val idLookup: AtomicIntShortSingleUseHashMap,
    private val store: AtomicVariableWidthArray,
    private val atomicPalette: AtomicIntArray,
    private val paletteCounter: AtomicInt,
    override val isPaletteMaxSize: Boolean
) : AbstractAtomicShortIntBackingArray(size) {
    init {
        if (previous != null) {
            copyFromPrevious(previous)
        }
    }

    override val palette: IntArray
        get() = toIntArray(atomicPalette, paletteCounter.value)
    override val backingArray: IntArray
        get() = store.packed
    override val paletteUsage: Int
        get() = paletteCounter.value

    override fun get(index: Int): Int = atomicPalette[store[index]].value

    override fun set(index: Int, value: Int): Int {
        val id = getId(value)
        val oldId = store.getAndSet(index, id)
        return atomicPalette[oldId].value
    }

    override fun compareAndSet(index: Int, expect: Int, update: Int): Boolean {
        val expectId = idLookup[expect]
        if (idLookup.isEmptyValue(expectId)) {
            return false
        }
        val newId = getId(update)
        return store.compareAndSet(index, expect, newId)
    }

    private fun getId(value: Int): Int {
        var id = idLookup[value].toInt()
        if (idLookup.isEmptyValue(id.toShort())) {
            id = paletteCounter.getAndIncrement()
            if (id >= paletteSize) {
                throw PaletteFullException()
            }
            val oldId = idLookup.putIfAbsent(value, id.toShort())
            if (!idLookup.isEmptyValue(oldId)) {
                id = oldId.toInt()
            }
            atomicPalette[id].value = value
        }
        return id
    }

    companion object {
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

        fun allowedPalette(length: Int) = length shr 2
    }
}

fun AtomicShortIntPaletteBackingArray(
    previous: AbstractAtomicShortIntBackingArray,
    compress: Boolean = false,
    expand: Boolean = false,
    unique: Int = CALCULATE_UNIQUE
) = AtomicShortIntPaletteBackingArray(previous.size, previous, compress, expand, unique)

fun AtomicShortIntPaletteBackingArray(
    size: Int,
    previous: AbstractAtomicShortIntBackingArray? = null,
    compress: Boolean = false,
    expand: Boolean = false,
    unique: Int = CALCULATE_UNIQUE
): AtomicShortIntPaletteBackingArray {
    val width = if (previous == null) {
        1
    } else {
        if (compress) {
            val calculatedUnique = if (unique == CALCULATE_UNIQUE) previous.unique else unique
            AtomicShortIntPaletteBackingArray.roundUpWith(if (expand) calculatedUnique else calculatedUnique - 1)
        } else {
            val oldWidth = previous.width
            when {
                oldWidth == 0 -> 1
                oldWidth <= 8 -> oldWidth shl 1
                else -> 16
            }
        }
    }
    val allowedPalette = allowedPalette(size)
    val paletteSize = min(widthToPaletteSize(width), allowedPalette)
    val paletteCounter = atomic(0)
    val idLookup = AtomicIntShortSingleUseHashMap(paletteSize + (paletteSize shr 2))
    if (previous == null) {
        paletteCounter.incrementAndGet()
        val isEmpty = idLookup.isEmptyValue(idLookup.putIfAbsent(0, 0))
        check(isEmpty) { "Entry was not zero when putting first element into HashMap" }
    }
    val isMaxPaletteSize = paletteSize == allowedPalette
    val store = AtomicVariableWidthArray(size, width)
    val palette = AtomicIntArray(paletteSize)
    return AtomicShortIntPaletteBackingArray(
        size, previous, width, paletteSize, idLookup, store, palette, paletteCounter, isMaxPaletteSize
    )
}

fun AtomicShortIntPaletteBackingArray(
    size: Int,
    unique: Int,
    initial: IntArray
): AtomicShortIntPaletteBackingArray {
    val width = roundUpWith(unique - 1)
    val allowedPalette = allowedPalette(size)
    val paletteSize = min(widthToPaletteSize(width), allowedPalette)
    val paletteCounter = atomic(0)
    val isMaxPaletteSize = paletteSize == allowedPalette
    val palette = AtomicIntArray(paletteSize)
    val store = AtomicVariableWidthArray(size, width)
    val idLookup = AtomicIntShortSingleUseHashMap(paletteSize + (paletteSize shr 2))
    return AtomicShortIntPaletteBackingArray(
        size,
        null,
        width,
        paletteSize,
        idLookup,
        store,
        palette,
        paletteCounter,
        isMaxPaletteSize
    ).apply {
        repeat(size) {
            set(it, initial[it])
        }
    }
}

fun AtomicShortIntPaletteBackingArray(
    size: Int,
    palette: IntArray,
    width: Int,
    variableWidthBlockArray: IntArray
): AtomicShortIntPaletteBackingArray {
    val allowedPalette = allowedPalette(size)
    val paletteSize = palette.size
    val paletteCounter = atomic(palette.size)
    val isMaxPaletteSize = paletteSize >= allowedPalette
    val atomicPalette = AtomicIntArray(palette.size).apply {
        palette.forEachIndexed { index, i ->
            get(index).value = i
        }
    }
    val store = AtomicVariableWidthArray(size, width, variableWidthBlockArray)
    val idLookup = AtomicIntShortSingleUseHashMap(paletteSize + (paletteSize shr 2))
    repeat(paletteSize) {
        idLookup.putIfAbsent(palette[it], it.toShort())
    }
    return AtomicShortIntPaletteBackingArray(
        size, null, width, paletteSize, idLookup, store, atomicPalette, paletteCounter, isMaxPaletteSize
    )
}
