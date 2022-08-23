package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Customer(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val transactions: List<Transaction> = emptyList()
)