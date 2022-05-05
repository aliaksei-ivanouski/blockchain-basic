package com.ivanouski.blockchain.wallet

import com.ivanouski.blockchain.block.BlockChain
import com.ivanouski.blockchain.support.CryptographyHelper
import com.ivanouski.blockchain.transaction.Transaction
import com.ivanouski.blockchain.transaction.TransactionInput
import java.security.PrivateKey
import java.security.PublicKey

class Wallet {

    var privateKey: PrivateKey
        private set
    var publicKey: PublicKey
        private set

    init {
        val keyPair = CryptographyHelper.ellipticCurveCrypto()
        this.publicKey = keyPair.public
        this.privateKey = keyPair.private
    }

    fun calculateBalance(): Double {
        var balance = 0.0
        BlockChain.UTXOs.entries.forEach { output ->
            output.value
                .takeIf {
                    it.isMine(this.publicKey)
                }
                ?.also {
                    balance += it.amount
                }
        }
        return balance
    }

    fun transferMoney(
        receiver: PublicKey,
        amount: Double
    ): Transaction? {
        if (calculateBalance() < amount) {
            println("Not enough money in the wallet...")
            return null
//            throw RuntimeException("Not enough money in the wallet.")
        }

        val inputs = mutableListOf<TransactionInput>()
        BlockChain.UTXOs.entries.forEach { output ->
            output.value
                .takeIf {
                    it.isMine(this.publicKey)
                }
                ?.also {
                    inputs.add(TransactionInput(it.id))
                }
        }

        val transaction = Transaction(
            sender = this.publicKey,
            receiver = receiver,
            amount = amount,
            inputs = inputs
        )
        transaction.generateSignature(this.privateKey)
        return transaction
    }
}
