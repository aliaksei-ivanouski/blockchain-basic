package com.ivanouski.blockchain.support

import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.MessageDigest
import java.security.PrivateKey
import java.security.PublicKey
import java.security.SecureRandom
import java.security.Signature
import java.security.spec.ECGenParameterSpec

object CryptographyHelper {

    fun generateHash(
        data: String
    ) = MessageDigest.getInstance("SHA-256")
        .digest(data.toByteArray(Charsets.UTF_8))
        .fold("") { str, it ->
            str + "%02x".format(it)
        }

    fun ellipticCurveCrypto(): KeyPair {
        try {
            val keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", "BC")
            val secureRandom = SecureRandom.getInstance("SHA1PRNG")
            val params = ECGenParameterSpec("secp192k1")
            keyPairGenerator.initialize(params, secureRandom)
            return keyPairGenerator.genKeyPair()
        } catch (e: Exception) {
            throw RuntimeException("Error occurred during key pair generation.")
        }
    }

    fun applyECDSASignature(
        privateKey: PrivateKey,
        input: String
    ): ByteArray = try {
        val signature = Signature.getInstance("ECDSA", "BC")
        signature.initSign(privateKey)
        signature.update(
            input.toByteArray(Charsets.UTF_8)
        )
        signature.sign()
    } catch (e: Exception) {
        throw RuntimeException("Problem occurred during signing process.")
    }

    fun verifyECDSASignature(
        publicKey: PublicKey,
        data: String,
        signature: ByteArray
    ): Boolean = try {
        val ecdsaSignature = Signature.getInstance("ECDSA", "BC")
        ecdsaSignature.initVerify(publicKey)
        ecdsaSignature.update(data.toByteArray(Charsets.UTF_8))
        ecdsaSignature.verify(signature)
    } catch (e: Exception) {
        throw RuntimeException("Problem occurred during signature verification process.")
    }
}
