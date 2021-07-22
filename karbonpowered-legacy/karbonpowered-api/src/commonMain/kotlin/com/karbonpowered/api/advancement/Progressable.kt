package com.karbonpowered.api.advancement

/**
 * Represents the status that is achieved or is in
 * the process of being achieved.
 */
interface Progressable {
    val time: Long?
    val achieved: Boolean get() = time != null
    val grant: Long
    fun revoke(): Long?
}