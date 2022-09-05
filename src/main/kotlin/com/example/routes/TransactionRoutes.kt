package com.example.routes

import com.example.db.Transactions
import com.example.db.transaction.TransactionDao
import com.example.models.Transaction
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.get

fun Route.transactionRouting(dao: TransactionDao) {
    get<Transactions.Id.New> {
        val id = call.parameters["id"] ?: return@get call.respondText(
            "Missing id",
            status = HttpStatusCode.BadRequest
        )

        val post = call.receive<Transaction>()
        val transaction = dao.addTransactionToCustomer(id.toInt(), post.item, post.price) ?:
        return@get call.respondText("Error", status = HttpStatusCode.InternalServerError)
        call.respond(HttpStatusCode.Created, transaction)
    }
    get<Transactions.Id.Receipt> {
        val id = call.parameters["id"] ?: return@get call.respondText(
            "Missing id",
            status = HttpStatusCode.BadRequest
        )
        val transaction = dao.getReceiptForCustomer(id.toInt()) ?: return@get call.respondText(
            "No customer with id $id",
            status = HttpStatusCode.NotFound
        )
        call.respond(transaction)
    }
}