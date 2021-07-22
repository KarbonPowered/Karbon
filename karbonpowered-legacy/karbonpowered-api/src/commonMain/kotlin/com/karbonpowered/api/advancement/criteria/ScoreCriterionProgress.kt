package com.karbonpowered.api.advancement.criteria


interface ScoreCriterionProgress : CriterionProgress {
    override val criterion: ScoreAdvancementCriterion

    val goal: Int get() = criterion.goal

    fun set(score: Int): Long?
    fun add(score: Int): Long?
    fun remove(score: Int): Long?
}