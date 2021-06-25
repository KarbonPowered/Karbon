package com.karbonpowered.engine.snapshot

import com.karbonpowered.common.collection.ConcurrentLinkedQueue
import io.ktor.util.*
import io.ktor.util.collections.*

@OptIn(InternalAPI::class)
class SnapshotableHashMap<K : Any, V : Any>(
    manager: SnapshotManager
) : Snapshotable, MutableMap<K, V> {
    init {
        manager.add(this)
    }

    private val live = ConcurrentMap<K, V>()
    private val snapshot = LinkedHashMap<K, V>()
    private val dirtyKeys = ConcurrentLinkedQueue<K>()
    private val dirtyValues = ConcurrentLinkedQueue<V>()

    val map: Map<K, V>
        get() = snapshot
    val liveMap: Map<K, V>
        get() = live

    override val size: Int
        get() = snapshot.size

    override fun containsKey(key: K): Boolean = snapshot.containsKey(key)

    override fun containsValue(value: V): Boolean = snapshot.containsValue(value)

    override fun get(key: K): V? = snapshot[key]

    override fun isEmpty(): Boolean = snapshot.isEmpty()

    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() = snapshot.toMutableMap().entries

    override val keys: MutableSet<K>
        get() = snapshot.keys.toMutableSet()

    override val values: MutableCollection<V>
        get() = snapshot.values.toMutableSet()

    override fun clear() {
        dirtyKeys.addAll(keys)
        dirtyValues.addAll(values)
        live.clear()
    }

    override fun put(key: K, value: V): V? {
        val oldValue = live.put(key, value)
        dirtyKeys.add(key)
        dirtyValues.add(value)
        return oldValue
    }

    override fun putAll(from: Map<out K, V>) {
        from.forEach { (key, value) ->
            put(key, value)
        }
    }

    override fun remove(key: K): V? {
        val oldValue = live.remove(key)
        if (oldValue != null) {
            dirtyKeys.add(key)
            dirtyValues.add(oldValue)
        }
        return oldValue
    }

    override fun copySnapshot() {
        dirtyKeys.forEach { key ->
            val value = live[key]
            if (value == null) {
                snapshot.remove(key)
            } else {
                snapshot[key] = value
            }
        }
        dirtyKeys.clear()
        dirtyValues.clear()
    }
}