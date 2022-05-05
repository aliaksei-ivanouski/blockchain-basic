package com.ivanouski.blockchain.block

import com.ivanouski.blockchain.transaction.TransactionOutput
import java.util.LinkedList

class BlockChain {
    companion object {
        var BLOCK_CHAIN: MutableList<Block> = LinkedList<Block>()
        var UTXOs = mutableMapOf<String, TransactionOutput>()
    }

    fun addBlock(block: Block) {
        BLOCK_CHAIN.add(block)
    }

    override fun toString() = BLOCK_CHAIN
        .joinToString(separator = "\n") {
            it.toString()
        }
}
