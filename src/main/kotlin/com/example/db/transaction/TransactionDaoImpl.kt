package com.example.db.transaction

import com.example.models.Transaction

class TransactionDaoImpl: TransactionDao {

    override suspend fun addTransactionToCustomer(customerId: Int): Transaction {
        TODO("Not yet implemented")
    }

    override suspend fun getTransactionsByCustomer(customerId: Int): List<Transaction> {
        TODO("Not yet implemented")
    }
}

fun TransactionEntity.mapToModel(): Transaction =
    Transaction(
        id = this.id.value,
        item = this.item,
        price = this.price,
        timestamp = this.timestamp
    )