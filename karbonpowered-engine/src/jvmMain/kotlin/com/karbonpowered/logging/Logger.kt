package com.karbonpowered.logging

actual object Logger {
    private const val ANSI_YELLOW = "\u001B[33m"
    private const val ANSI_RED = "\u001B[31m"

    actual fun info(message: String) {
        println(message)
    }

    actual fun warn(message: String) {
        println(ANSI_YELLOW+message)
    }

    actual fun error(message: String, throwable: Throwable?) {
        System.err.println(ANSI_RED+message)
        throwable?.printStackTrace(System.err)
    }
}