package com.ivanouski.blockchain.transaction

data class TransactionInput(
    val transactionOutputId: String,
    var utxo: TransactionOutput? = null
)
