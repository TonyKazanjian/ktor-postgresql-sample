package com.example.db

import com.example.models.Customer

interface CustomerDAO {
    suspend fun allCustomers(): List<Customer>
    suspend fun customer(id: Int): Customer?
    suspend fun addNewCustomer(firstName: String, lastName: String, email: String): Customer?
}