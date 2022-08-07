package com.example.db

import org.jetbrains.exposed.dao.id.IntIdTable

object CustomersTable : IntIdTable() {
    val firstName = varchar("firstName", 128)
    val lastName = varchar("lastName", 128)
    val email = varchar("email", 128)
}