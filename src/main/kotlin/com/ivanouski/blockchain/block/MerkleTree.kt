package com.ivanouski.blockchain.block

import com.ivanouski.blockchain.support.CryptographyHelper

class MerkleTree(
    private var transactions: List<String>
) {

    fun getMerkleRoot() = construct(transactions).first()

    private fun construct(
        transactions: List<String>
    ): List<String> {
        if (transactions.size == 1) return transactions

        val updatedList = mutableListOf<String>()
        for (i in transactions.indices step 2) {
            if (transactions.size - 1 > i) {
                updatedList.add(
                    mergeHash(
                        transactions[i],
                        transactions[i + 1]
                    )
                )
            }
        }

        if (transactions.size % 2 == 1) {
            updatedList.add(
                mergeHash(
                    transactions[transactions.indices.last],
                    transactions[transactions.indices.last]
                )
            )
        }

        return construct(updatedList)
    }

    private fun mergeHash(
        sha1: String,
        sha2: String
    ) = CryptographyHelper.generateHash("$sha1$sha2")

}
