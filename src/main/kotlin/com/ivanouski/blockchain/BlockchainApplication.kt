package com.ivanouski.blockchain

import com.ivanouski.blockchain.block.Block
import com.ivanouski.blockchain.block.BlockChain
import com.ivanouski.blockchain.miner.Miner
import com.ivanouski.blockchain.support.Constants
import com.ivanouski.blockchain.transaction.Transaction
import com.ivanouski.blockchain.transaction.TransactionOutput
import com.ivanouski.blockchain.wallet.Wallet
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.springframework.boot.autoconfigure.SpringBootApplication
import java.security.Security

@SpringBootApplication
class BlockchainApplication

fun main(args: Array<String>) {
//	runApplication<BlockchainApplication>(*args)

    Security.addProvider(BouncyCastleProvider())


    val userA = Wallet()
    val userB = Wallet()
    val lender = Wallet()
    val blockChain = BlockChain()
    val miner = Miner()

    val genesisTransaction = Transaction(
        sender = lender.publicKey,
        receiver = userA.publicKey,
        amount = 500.0
    )
    genesisTransaction.generateSignature(lender.privateKey)
//	genesisTransaction.id = 0
    genesisTransaction.outputs.add(
        TransactionOutput(
            genesisTransaction.id,
            genesisTransaction.receiver,
            genesisTransaction.amount
        )
    )
    BlockChain.UTXOs[genesisTransaction.outputs[0].id] = genesisTransaction.outputs[0]

    val genesis = Block(id = 0, previousHash = Constants.GENESIS_PREV_HASH)
    genesis.addTransaction(genesisTransaction)
    miner.mine(genesis, blockChain)

    println()
    val block1 = Block(id = 1, previousHash = genesis.hash)
    userA.transferMoney(userB.publicKey, 120.0)
        ?.let {
            block1.addTransaction(it)
        }
    miner.mine(block1, blockChain)
    println("userA balance is: ${userA.calculateBalance()}")
    println("userB balance is: ${userB.calculateBalance()}")

    println()
    val block2 = Block(id = 2, previousHash = block1.hash)
    userA.transferMoney(userB.publicKey, 600.0)
        ?.let {
            block2.addTransaction(it)
        }
    miner.mine(block2, blockChain)
    println("userA balance is: ${userA.calculateBalance()}")
    println("userB balance is: ${userB.calculateBalance()}")

    println()
    val block3 = Block(id = 3, previousHash = block2.hash)
    userB.transferMoney(userA.publicKey, 120.0)
        ?.let {
            block3.addTransaction(it)
        }
    println("userA balance is: ${userA.calculateBalance()}")
    println("userB balance is: ${userB.calculateBalance()}")
    miner.mine(block3, blockChain)

    println()
    println("miner reward is: ${miner.reward}")
}
