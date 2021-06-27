package com.karbonpowered.common.hash

inline fun hashCode(vararg any: Any?): Int {
    var result = 0
    any.forEach {
        result = (result + it.hashCode()) * 31
    }
    return result
}