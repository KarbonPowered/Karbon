package com.karbonpowered.common

import com.karbonpowered.common.collection.concurrent.map.TripleIntObjectReferenceArrayMap
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TripleIntObjectReferenceArrayMapTest {

    private val EDGEX = 3
    private val EDGEY = 3
    private val EDGEZ = 640

    private val map = TripleIntObjectReferenceArrayMap<FakeObject>(3)

    @Test
    fun test() {
        val objects = Array(EDGEX * EDGEY * EDGEZ) { FakeObject() }
        val halfEdgeX = EDGEX shr 1
        val halfEdgeY = EDGEY shr 1
        val halfEdgeZ = EDGEZ shr 1

        var i = 0
        repeat(EDGEX) { x ->
            repeat(EDGEY) { y ->
                repeat(EDGEZ) { z ->
                    objects[i++] = FakeObject(x - halfEdgeX, y - halfEdgeY, z - halfEdgeZ)
                }
            }
        }

        objects.shuffle()
        objects.forEach {
            map[it.x, it.y, it.z] = it
        }

        objects.shuffle()
        objects.forEach {
            assertEquals(it, map[it.x, it.y, it.z])
        }

        for (x in -halfEdgeX until halfEdgeX) {
            for (y in -halfEdgeY until halfEdgeY) {
                for (z in -halfEdgeZ until halfEdgeZ) {
                    val fakeObject = checkNotNull(map[x, y, z])
                    assertTrue(fakeObject.test(x, y, z))
                }
            }
        }

        val valuesCopy = LinkedHashSet(map.values)
        for (x in -halfEdgeX until halfEdgeX) {
            for (y in -halfEdgeY until halfEdgeY) {
                for (z in -halfEdgeZ until halfEdgeZ) {
                    val fakeObject = checkNotNull(map[x, y, z])
                    assertTrue(valuesCopy.contains(fakeObject))
                }
            }
        }
    }

    data class FakeObject(
        val x: Int = 0,
        val y: Int = 0,
        val z: Int = 0,
        val hash: Int = 0
    ) {
        fun test(x: Int, y: Int, z: Int) = this.x == x && this.y == y && this.z == z
    }
}