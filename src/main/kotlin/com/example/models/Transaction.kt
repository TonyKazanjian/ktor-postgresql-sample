package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Transaction(val id: Int, val item: String, val price: Float, val timestamp: Long)

@Serializable
data class Receipt(val transactions: List<Transaction>, val total: Float)
