package com.karbonpowered.server.crypt

interface PacketEncryption {
    fun decryptOutputSize(length: Int)

    fun encryptOutputSize(length: Int)

    fun decrypt(input: ByteArray, inputOffset: Int = 0, inputLength: Int, output: ByteArray, outputOffset: Int)

    fun encrypt(input: ByteArray, inputOffset: Int = 0, inputLength: Int, output: ByteArray, outputOffset: Int)
}