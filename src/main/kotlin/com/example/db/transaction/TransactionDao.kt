package com.example.db.transaction

import com.example.models.Transaction

interface TransactionDao {
    suspend fun addTransactionToCustomer(
        customerId: Int,
        item: String,
        price: Float
    ): Transaction?
    suspend fun getTransactionsByCustomer(customerId: Int): List<Transaction>?

    //TODO - get receipt
}