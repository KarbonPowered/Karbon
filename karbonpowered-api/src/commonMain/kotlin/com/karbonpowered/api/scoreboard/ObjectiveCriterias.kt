package com.karbonpowered.api.scoreboard

object ObjectiveCriterias {
    val DUMMY = object : ObjectiveCriteria() { override val name = "dummy" }
    val TRIGGER = object : ObjectiveCriteria() { override val name = "trigger" }
    val DEATH_COUNT = object : ObjectiveCriteria() { override val name = "deathCount" }
    val KILL_COUNT_PLAYERS = object : ObjectiveCriteria() { override val name = "playerKillCount" }
    val KILL_COUNT_ALL = object : ObjectiveCriteria() { override val name = "totalKillCount" }
    val HEALTH = object : ObjectiveCriteria() {
        override val name = "health"
        override val readOnly = true
    }
    val FOOD = object : ObjectiveCriteria() {
        override val name = "food"
        override val readOnly = true
    }
    val AIR = object : ObjectiveCriteria() {
        override val name = "air"
        override val readOnly = true
    }
    val ARMOR = object : ObjectiveCriteria() {
        override val name = "armor"
        override val readOnly = true
    }
    val EXPERIENCE = object : ObjectiveCriteria() {
        override val name = "experience"
        override val readOnly = true
    }
    val LEVEL = object : ObjectiveCriteria() {
        override val name = "level"
        override val readOnly = true
    }
}