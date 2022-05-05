package com.ivanouski.blockchain.block

import com.ivanouski.blockchain.support.Constants
import com.ivanouski.blockchain.support.CryptographyHelper
import com.ivanouski.blockchain.transaction.Transaction
import java.util.Date

data class Block(
    val id: Int = 0,
    var nonce: Int = 0,
    val transactions: MutableList<Transaction> = mutableListOf(),
    val merkleRoot: String = "",
    var hash: String = "",
    var previousHash: String,
    val timestamp: Long = Date().time
) {

    init {
        generateHash()
    }

    fun incrementNonce() = this.nonce++

    fun generateHash() {
        this.hash = CryptographyHelper.generateHash(
            "${this.id}${this.previousHash}${this.timestamp}${this.transactions}${this.nonce}"
        )
    }

    fun addTransaction(
        transaction: Transaction
    ): Boolean {
        if (this.previousHash != Constants.GENESIS_PREV_HASH) {
            if (!transaction.verifyTransaction()) {
                return false
            }
        }
        return transactions.add(transaction)
    }

    override fun toString(): String {
        return "Block(id=$id, hash='$hash', previousHash='$previousHash', timestamp=$timestamp)"
    }
}
