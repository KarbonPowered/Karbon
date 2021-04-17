package com.karbonpowered.api.advancement

interface TreeLayout {
    val tree: AdvancementTree
    val elements: Collection<TreeLayoutElement>
    fun element(advancement: Advancement): TreeLayoutElement?
}