package com.karbonpowered.engine.world

import com.karbonpowered.engine.scheduler.AsyncManager
import com.karbonpowered.engine.util.BitSize
import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic

/**
 * Represents a cube containing 16x16x16 Chunks (256x256x256 Blocks)
 */
class KarbonRegion(
    val world: KarbonWorld,
    val x: Int,
    val y: Int,
    val z: Int,
    val regionSource: RegionSource
) : AsyncManager {
    private val chunks: Array<Array<Array<AtomicRef<KarbonChunk?>>>> =
        Array(CHUNKS.SIZE) { Array(CHUNKS.SIZE) { Array(CHUNKS.SIZE) { atomic(null) } } }
    private val neighbours = Array(3) { dx ->
        Array(3) { dy ->
            Array(3) { dz ->
                atomic(world.getRegion(x + dx - 1, y + dy - 1, z + dz - 1, LoadOption.NO_LOAD))
            }
        }
    }

    fun unlinkNeighbours() {
        repeat(3) { dx ->
            repeat(3) { dy ->
                repeat(3) { dz ->
                    val region = world.getRegion(x + dx - 1, y + dy - 1, z + dz - 1, LoadOption.NO_LOAD)
                    region?.unlinkNeighbour(this)
                }
            }
        }
    }

    private fun unlinkNeighbour(region: KarbonRegion) {
        repeat(3) { dx ->
            repeat(3) { dy ->
                repeat(3) { dz ->
                    neighbours[dx][dy][dz].compareAndSet(region, null)
                }
            }
        }
    }

    fun getLocalRegion(dx: Int, dy: Int, dz: Int, loadOption: LoadOption = LoadOption.NO_LOAD): KarbonRegion? {
        val ref = neighbours[dx][dy][dy]
        var region = ref.value
        if (region == null) {
            region = world.getRegion(x + dx - 1, y + dy - 1, z + dz - 1, loadOption)
            ref.compareAndSet(null, region)
        }
        return region
    }

    fun getChunk(x: Int, y: Int, z: Int, loadOption: LoadOption): KarbonChunk {
        TODO("Not yet implemented")
    }

    companion object {
        /**
         * Stores the size of the amount of chunks in this Region
         */
        val CHUNKS = BitSize(4)

        /**
         * Stores the size of the amount of blocks in this Region
         */
        val BLOCKS = BitSize(CHUNKS.BITS + KarbonChunk.BLOCKS.BITS)
    }
}
