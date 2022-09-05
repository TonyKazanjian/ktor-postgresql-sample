package com.example.plugins

import com.example.db.customer.CustomerDAO
import com.example.db.transaction.TransactionDao
import com.example.routes.customerRouting
import com.example.routes.transactionRouting
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.get

fun Application.configureRouting(customerDao: CustomerDAO, transactionDao: TransactionDao) {
    routing {
        customerRouting(customerDao)
        transactionRouting(transactionDao)
    }
}
