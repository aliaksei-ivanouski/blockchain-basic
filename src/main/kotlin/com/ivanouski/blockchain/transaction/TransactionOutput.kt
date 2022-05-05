package com.ivanouski.blockchain.transaction

import com.ivanouski.blockchain.support.CryptographyHelper
import java.security.PublicKey

data class TransactionOutput(
    val parentTransactionId: String,
    val receiver: PublicKey? = null,
    val amount: Double
) {
    val id = CryptographyHelper.generateHash("$receiver$amount$parentTransactionId")

    fun isMine(
        publicKey: PublicKey
    ) = this.receiver == publicKey
}
