package com.example.db

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object TransactionTable: IntIdTable() {
    val item = varchar("item", 128)
    val price = float("price")
    val customer = reference("customer", CustomersTable)
}

class TransactionEntity(id: EntityID<Int>): IntEntity(id){
    companion object : IntEntityClass<TransactionEntity>(TransactionTable)
    var item by TransactionTable.item
    var price by TransactionTable.price
    var customer by CustomerEntity referencedOn TransactionTable.customer
}