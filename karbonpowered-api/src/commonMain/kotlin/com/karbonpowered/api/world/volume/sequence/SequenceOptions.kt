package com.karbonpowered.api.world.volume.sequence

import com.karbonpowered.api.world.World
import com.karbonpowered.api.world.volume.Volume

interface SequenceOptions {
    /**
     * Whether a carbon copy of the area requested for the stream will be
     * offloaded into a separated [Volume] such that the stream would
     * be unaffected by other operations being performed in the targeted
     * [Volume] (like block replacements). This may be expensive to
     * perform overall on a larger scale, but effectively can be safer to
     * perform sensitive filtering operations.
     *
     * @return `true` if a copy of the volume's stream targets will be made
     */
    val carbonCopy: Boolean

    /**
     * Determines the loading style of sections to populate elements for a Stream,
     * such that the area may be pre-calculated or the elements precalculated based
     */
    val loadingStyle: LoadingStyle

    companion object {
        fun builder(): Builder = TODO()
        fun builder(builder: Builder.() -> Unit): SequenceOptions = builder().apply(builder).build()

        fun lazily(): SequenceOptions = builder {
            carbonCopy = false
            loadingStyle = LoadingStyle.LAZILY_UNGENERATED
        }

        fun forceLoadedAndCopied(): SequenceOptions = builder {
            carbonCopy = true
            loadingStyle = LoadingStyle.FORCED_GENERATED
        }
    }

    interface Builder {
        var carbonCopy: Boolean
        var loadingStyle: LoadingStyle

        fun carbonCopy(copies: Boolean) = apply {
            this.carbonCopy = copies
        }

        fun loadingStyle(style: LoadingStyle) = apply {
            this.loadingStyle = loadingStyle
        }

        fun build(): SequenceOptions
    }

    enum class LoadingStyle(
            val generateArea: Boolean,
            val immediateLoading: Boolean
    ) {
        /**
         * Forces the loading of the entire area to calculate the exact
         * available sections to fetch the predefined stream of elements
         * to populate the [VolumeSequence]. This will incur an initial
         * cost of [World.loadChunk]
         * with `shouldGenerate = [generateArea]`.
         */
        FORCED_GENERATED(true, true),

        /**
         * Forces the loading of the entire area to calculate the exact
         * available sections to fetch the predefined stream of elements
         * to populate the [VolumeSequence]. This will incur an initial
         * cost of [World.loadChunk]
         * with `shouldGenerate = [generateArea]`.
         */
        FORCED_UNGENERATED(false, true),

        /**
         * Ensures that sections within the area are loaded during stream
         * execution, but unlike [FORCED_GENERATED], sections will only be
         * loaded during [VolumeSequence] execution if and when the
         * section is iterated over, and not pre-loaded beforehand.
         *
         * In the case of a [World],
         * this will still issue a request for a [World.loadChunk]
         * with `shouldGenerate = [generateArea]`.
         */
        LAZILY_UNGENERATED(false, false),

        /**
         * Ensures that sections within the area are loaded during stream
         * execution, but unlike [FORCED_GENERATED], sections will only be
         * loaded during [VolumeSequence] execution if and when the
         * section is iterated over, and not pre-loaded beforehand.
         *
         * In the case of a [World],
         * this will still issue a request for a [World.loadChunk]
         * with `shouldGenerate = [generateArea]`.
         */
        LAZILY_GENERATED(true, false),

        /**
         * Only requests the available sections of the [Volume]
         * without incurring any loading of sections costs, useful for
         * building streams of available elements.
         */
        NONE(false, false)
    }
}