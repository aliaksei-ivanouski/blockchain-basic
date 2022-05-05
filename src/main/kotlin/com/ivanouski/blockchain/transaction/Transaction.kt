package com.ivanouski.blockchain.transaction

import com.ivanouski.blockchain.support.CryptographyHelper
import com.ivanouski.blockchain.block.BlockChain
import java.security.PrivateKey
import java.security.PublicKey

data class Transaction(
    private val sender: PublicKey,
    public val receiver: PublicKey,
    private var signature: ByteArray? = null,
    public val amount: Double,
    private val inputs: MutableList<TransactionInput> = mutableListOf(),
    public val outputs: MutableList<TransactionOutput> = mutableListOf()
) {
    private val baseString = "${this.sender}${this.receiver}${this.amount}"

    val id = CryptographyHelper.generateHash(baseString)

    fun verifyTransaction(): Boolean {
        if (!verifySignature()) return false

        this.inputs.forEach {
            it.utxo = BlockChain.UTXOs[it.transactionOutputId]!!
        }

        this.outputs.add(TransactionOutput(this.id, this.receiver, this.amount))
        this.outputs.add(TransactionOutput(this.id, this.sender, getInputSum() - this.amount))

        this.inputs.forEach { input ->
            input.utxo?.let {
                BlockChain.UTXOs.remove(it.id)
            }
        }

        this.outputs.forEach {
            BlockChain.UTXOs[it.id] = it
        }

        return true
    }

    private fun getInputSum(): Double {
        var sum = 0.0
        this.inputs.forEach { input ->
            input.utxo?.let {
                sum += it.amount
            }
        }
        return sum
    }

    fun generateSignature(
        privateKey: PrivateKey
    ) {
        this.signature = CryptographyHelper.applyECDSASignature(privateKey, baseString)
    }

    private fun verifySignature(): Boolean {
        this.signature ?: return false
        return CryptographyHelper.verifyECDSASignature(sender, baseString, signature!!)
    }
}
