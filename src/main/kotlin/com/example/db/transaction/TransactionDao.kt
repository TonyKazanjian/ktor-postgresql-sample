package com.example.db.transaction

import com.example.models.Receipt
import com.example.models.Transaction

interface TransactionDao {
    suspend fun addTransactionToCustomer(
        customerId: Int,
        item: String,
        price: Float
    ): Transaction?

    suspend fun getReceiptForCustomer(customerId: Int): Receipt?
}