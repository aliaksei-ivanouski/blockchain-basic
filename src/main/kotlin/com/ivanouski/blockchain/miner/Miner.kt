package com.ivanouski.blockchain.miner

import com.ivanouski.blockchain.block.Block
import com.ivanouski.blockchain.block.BlockChain
import com.ivanouski.blockchain.support.Constants

class Miner {

    var reward: Double = 0.0

    fun mine(
        block: Block,
        blockChain: BlockChain
    ) {
        while (!isGoldenHash(block)) {
            block.incrementNonce()
            block.generateHash()
        }

        println("$block has just mined...")
        println("Hash is: ${block.hash}")

        blockChain.addBlock(block)
        reward += Constants.REWARD
    }

    private fun isGoldenHash(
        block: Block
    ) = block.hash
        .substring(0, Constants.DIFFICULTY) == String(CharArray(Constants.DIFFICULTY))
        .replace(Char(0), '0')
}
