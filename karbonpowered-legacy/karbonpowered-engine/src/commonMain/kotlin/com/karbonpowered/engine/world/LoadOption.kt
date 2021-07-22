package com.karbonpowered.engine.world

enum class LoadOption(
    val isLoad: Boolean,
    val isGenerate: Boolean
) {
    /**
     * Do not load or generate chunk/region if not currently loaded
     */
    NO_LOAD(false, false),

    /**
     * Load chunk/region if not currently loaded,
     * but do not generate it if it does not yet exist
     */
    LOAD_ONLY(true, false),

    /**
     * Load chunk/region if not currently loaded,
     * and generate it if it does not yet exist
     */
    LOAD_GEN(true, true),

    /**
     * Don't load the chunk if it has already been generated,
     * only generate if it does not yet exist
     */
    GEN_ONLY(false, true)
}
