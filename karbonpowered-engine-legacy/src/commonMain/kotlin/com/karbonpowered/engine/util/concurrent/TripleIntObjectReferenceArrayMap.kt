package com.karbonpowered.engine.util.concurrent

import kotlinx.atomicfu.AtomicArray
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.atomicArrayOfNulls
import kotlinx.atomicfu.locks.reentrantLock
import kotlinx.atomicfu.locks.withLock
import kotlin.jvm.Synchronized


interface TripleIntObjectMap<T> {
    val values: Collection<T>

    operator fun get(x: Int, y: Int, z: Int): T?

    fun remove(x: Int, y: Int, z: Int): T?

    fun remove(x: Int, y: Int, z: Int, value: T): Boolean

    operator fun set(x: Int, y: Int, z: Int, value: T): T?

    fun putIfAbsent(x: Int, y: Int, z: Int, value: T): T?
}

class TripleIntObjectReferenceArrayMap<T>(
    val bits: Int, val depth: Int
) : TripleIntObjectMap<T> {
    private val lock = reentrantLock()
    val doubleBits = bits shl 1
    val width = 1 shl bits
    val bitMask = width - 1
    val arraySize = width * width * width
    val root = atomic<Entry<T>>(AtomicReferenceArrayEntry(depth))
    val valuesSnapshot = atomic<Collection<T>?>(null)
    private val _values = LinkedHashSet<T>()
    val leafEntries = LinkedHashSet<LeafEntry>()
    var removed = 0
    override val values: Collection<T>
        get() {
            var newValues = valuesSnapshot.value
            if (newValues != null) return newValues
            return lock.withLock {
                newValues = valuesSnapshot.value
                if (newValues == null) {
                    newValues = LinkedHashSet(_values)
                    valuesSnapshot.value = newValues
                }
                TODO()
            }
        }

    override fun get(x: Int, y: Int, z: Int): T? = getEntryRaw(x, y, z)?.value

    override fun remove(x: Int, y: Int, z: Int): T? = lock.withLock {
        getEntryRaw(x, y, z)?.let { entry ->
            val value = entry.remove()
            if (value != null) {
                valuesSnapshot.value = null
                removed++
                check(_values.remove(value)) { "Item removed from map was not in item set" }
            }
            value
        }
    }

    override fun remove(x: Int, y: Int, z: Int, value: T): Boolean = lock.withLock {
        getEntryRaw(x, y, z)?.let { entry ->
            val result = entry.remove(value)
            TODO()
//            if (result) {
//                valuesSnapshot.value = null
//                removed++
//                check(_values.remove(result)) { "Item removed from map was not in item set" }
//            }
            result
        } ?: false
    }

    override fun set(x: Int, y: Int, z: Int, value: T): T? = lock.withLock {
        val entry = getOrCreateEntry(x, y, z) ?: throw IllegalStateException("Unable to create entry")
        val old = entry.put(value)
        valuesSnapshot.value = null
        check(_values.add(value)) { "Failed to add item to the value set, items may only be added once to the map" }
        if (old != null) {
            check(_values.remove(old)) { "Item removed from map was not in item set" }
        }
        old
    }

    override fun putIfAbsent(x: Int, y: Int, z: Int, value: T): T? = lock.withLock {
        val entry = getOrCreateEntry(x, y, z) ?: throw IllegalStateException("Unable to create entry for put")
        val old = entry.putIfAbsent(value)
        if (old == null) {
            valuesSnapshot.value = null
            check(_values.add(value)) {
                "Failed to add item to the value set, items may only be added once to the map"
            }
        }
        old
    }

    private fun getOrCreateEntry(x: Int, y: Int, z: Int): Entry<T>? {
        var entry = getEntryRaw(x, y, z)
        if (entry != null) {
            return entry
        }
        entry = root.value
        val depth: Int = entry.depth
        var shift: Int = entry.initialShift
        var prevEntry: Entry<T>? = null
        var keyDepth = 0
        while (entry != null) {
            prevEntry = entry
            entry = entry.getSubEntry(x, y, z, shift)
            shift -= bits
            if (entry == null) {
                break
            }
            keyDepth++
        }

        // Map must be resized if we hit a leaf node (collision) or the map was already at max depth
        if (keyDepth > depth || prevEntry is LeafEntry) {
            resizeMap()
            return getOrCreateEntry(x, y, z)
        } else check(keyDepth <= depth) { "Map has a depth that exceeds the depth variable" }
        entry = prevEntry
        shift += bits
        for (i in keyDepth until depth) {
            val newEntry = AtomicReferenceArrayEntry(depth)
            check(
                (entry as AtomicReferenceArrayEntry).addNewEntry(
                    x,
                    y,
                    z,
                    shift,
                    newEntry
                )
            ) { "Unable to add new map entry" }
            shift -= bits
            entry = newEntry
        }
        check(shift == 0) { "Shift counter in illegal state: $shift" }
        val newEntry = LeafEntry(x, y, z)
        check((entry as AtomicReferenceArrayEntry).addNewEntry(x, y, z, 0, newEntry)) { "Unable to add new leaf entry" }
        leafEntries.add(newEntry)
        return newEntry
    }

    @Synchronized
    private fun resizeMap() {
        val oldRoot: Entry<T> = root.value
        val newDepth: Int = oldRoot.depth + 1
        val temp = TripleIntObjectReferenceArrayMap<T>(bits, newDepth)
        for (le in leafEntries) {
            val value = le.value
            if (value != null) {
                val x = le.x
                val y = le.y
                val z = le.z
                temp[x, y, z] = value
                check(_values.remove(value)) { "Value moved to other map on resize was not in value list" }
            }
        }
        check(_values.isEmpty()) { "Some values were not transferred to the new map on resize" }
        leafEntries.clear()
        leafEntries.addAll(temp.leafEntries)
        _values.addAll(temp._values)
        check(root.compareAndSet(oldRoot, temp.root.value)) { "Old root changed while resizing" }
    }

    private fun getEntryRaw(x: Int, y: Int, z: Int): Entry<T>? {
        var entry = root.value
        val depth = entry.depth
        var shift = entry.initialShift
        repeat(depth) {
            entry.getSubEntry(x, y, z, shift)?.let {
                entry = it
                shift -= bits
            } ?: return null
        }
        return if (entry.testKey(x, y, z)) {
            entry
        } else {
            null
        }
    }

    interface Entry<T> {
        fun getSubEntry(x: Int, y: Int, z: Int, shift: Int): Entry<T>?

        fun testKey(x: Int, y: Int, z: Int): Boolean

        val value: T?

        fun remove(): T?

        fun remove(value: T): Boolean

        fun putIfAbsent(value: T): T?

        fun put(value: T): T?

        val depth: Int

        val initialShift: Int
    }

    inner class AtomicReferenceArrayEntry(
        override val depth: Int,
        val array: AtomicArray<Entry<T>?> = atomicArrayOfNulls(arraySize),
        override val initialShift: Int = depth * bits
    ) : Entry<T> {
        override val value: T
            get() = throw UnsupportedOperationException("The AtomicReferenceArrayEntry class cannot store values directly")

        override fun getSubEntry(x: Int, y: Int, z: Int, shift: Int): Entry<T>? = array[getIndex(x, y, z, shift)].value

        override fun testKey(x: Int, y: Int, z: Int): Boolean =
            throw UnsupportedOperationException("The AtomicReferenceArrayEntry class does not contain key/value pairs")

        override fun remove(): T =
            throw UnsupportedOperationException("The AtomicReferenceArrayEntry class does not contain key/value pairs")

        override fun remove(value: T): Boolean =
            throw UnsupportedOperationException("The AtomicReferenceArrayEntry class does not contain key/value pairs")

        override fun putIfAbsent(value: T): T =
            throw UnsupportedOperationException("The AtomicReferenceArrayEntry class does not contain key/value pairs")

        override fun put(value: T): T =
            throw UnsupportedOperationException("The AtomicReferenceArrayEntry class does not contain key/value pairs")

        fun addNewEntry(x: Int, y: Int, z: Int, shift: Int, subEntry: Entry<T>): Boolean =
            array[getIndex(x, y, z, shift)].compareAndSet(null, subEntry)
    }

    inner class LeafEntry(
        val x: Int,
        val y: Int,
        val z: Int
    ) : Entry<T> {
        private val _value = atomic<T?>(null)
        override val value: T?
            get() = _value.value

        override fun getSubEntry(x: Int, y: Int, z: Int, shift: Int): Entry<T>? = null

        override fun testKey(x: Int, y: Int, z: Int): Boolean = x == this.x && y == this.y && z == this.z

        override fun remove(): T? {
            return _value.getAndSet(null)
        }

        override fun remove(value: T): Boolean {
            return _value.compareAndSet(value, null)
        }

        override fun putIfAbsent(value: T): T? {
            while (true) {
                val old = _value.value
                if (old != null) {
                    return old
                }
                if (_value.compareAndSet(null, value)) {
                    return null
                }
            }
        }

        override fun put(value: T): T? = _value.getAndSet(value)

        override val depth: Int
            get() = throw UnsupportedOperationException("The LeafEntry class does not support this method")
        override val initialShift: Int
            get() = throw UnsupportedOperationException("The LeafEntry class does not support this method")
    }

    private fun getIndex(x: Int, y: Int, z: Int, shift: Int): Int {
        var dx = x shr shift
        var dy = y shr shift
        var dz = z shr shift
        dx = dx and bitMask
        dy = dy and bitMask
        dz = dz and bitMask
        return dx and bitMask shl doubleBits or (dy and bitMask shl bits) or (dz and bitMask)
    }
}