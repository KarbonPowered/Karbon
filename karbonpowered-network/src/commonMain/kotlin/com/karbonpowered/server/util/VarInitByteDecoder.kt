package com.karbonpowered.server.util

class VarInitByteDecoder {
    var readVarInt = 0
    private set
    var bytesRead = 0
    var result = DecodeResult.TOO_SHORT

    fun process(byte: Int): Boolean {
        readVarInt = readVarInt or (byte and 0x7F shl bytesRead++ * 7)
        if (bytesRead > 3) {
            result = DecodeResult.TOO_BIG
            return false
        }
        if (byte and 0x80 != 128) {
            result = DecodeResult.SUCCESS
            return false
        }
        return true
    }

    fun reset() {
        readVarInt = 0
        bytesRead = 0
        result = DecodeResult.TOO_SHORT
    }

    enum class DecodeResult {
        SUCCESS,
        TOO_SHORT,
        TOO_BIG
    }
}