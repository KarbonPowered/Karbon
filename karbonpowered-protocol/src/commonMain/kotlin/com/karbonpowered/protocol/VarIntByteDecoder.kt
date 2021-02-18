package com.karbonpowered.protocol

class VarIntByteDecoder {
    var readVarInt: Int = 0
        private set
    var bytesRead: Int = 0
        private set
    var result = Result.TOO_SHORT
        private set

    fun process(b: Int): Boolean {
        readVarInt = readVarInt or (b and 0x7F shl bytesRead++ * 7)
        if (bytesRead > 3) {
            result = Result.TOO_BIG
            return false
        }
        if (b and 0x80 != 128) {
            result = Result.SUCCESS
            return false
        }
        return true
    }

    fun reset() {
        readVarInt = 0
        bytesRead = 0
        result = Result.TOO_SHORT
    }

    enum class Result {
        SUCCESS, TOO_SHORT, TOO_BIG
    }
}