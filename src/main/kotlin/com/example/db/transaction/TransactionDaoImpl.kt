package com.example.db.transaction

import com.example.db.DatabaseFactory.dbQuery
import com.example.db.customer.CustomerEntity
import com.example.models.Receipt
import com.example.models.Transaction
import org.jetbrains.exposed.sql.mapLazy

class TransactionDaoImpl: TransactionDao {

    override suspend fun addTransactionToCustomer(
        customerId: Int,
        item: String,
        price: Float
    ): Transaction  = dbQuery {
        val customer = CustomerEntity.findById(customerId)
        val transaction = TransactionEntity.new {
            this.item = item
            this.price = price
            timestamp = System.currentTimeMillis()
            this.customer = customer!!
        }
        transaction.mapToModel()
    }

    override suspend fun getReceiptForCustomer(customerId: Int): Receipt? = dbQuery {
        val customer = CustomerEntity.findById(customerId)
        customer?.transactions?.mapLazy { it.mapToModel() }?.toList()?.let {
            Receipt(transactions = it, total = it.map { transaction -> transaction.price }.sum())
        }
    }
}

fun TransactionEntity.mapToModel(): Transaction =
    Transaction(
        id = this.id.value,
        item = this.item,
        price = this.price,
        timestamp = this.timestamp
    )