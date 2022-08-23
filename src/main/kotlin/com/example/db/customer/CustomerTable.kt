package com.example.db.customer

import com.example.db.transaction.TransactionEntity
import com.example.db.transaction.TransactionTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object CustomersTable : IntIdTable() {
    val firstName = varchar("firstName", 128)
    val lastName = varchar("lastName", 128)
    val email = varchar("email", 128)
}

class CustomerEntity(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<CustomerEntity>(CustomersTable)
    var firstName by CustomersTable.firstName
    var lastName by CustomersTable.lastName
    var email by CustomersTable.email
    val transactions by TransactionEntity referrersOn TransactionTable.customer
}