package com.example.routes

import com.example.db.transaction.TransactionDao
import com.example.models.Transaction
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.transactionRouting(dao: TransactionDao) {
    route("/transaction"){
        get("{id?}"){
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            val transaction = dao.getTransactionsByCustomer(id.toInt()) ?: return@get call.respondText(
                "No customer with id $id",
                status = HttpStatusCode.NotFound
            )
            call.respond(transaction)
        }
        post("{id?}") {
            val id = call.parameters["id"] ?: return@post call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )

            val post = call.receive<Transaction>()
            val transaction = dao.addTransactionToCustomer(id.toInt(), post.item, post.price) ?:
            return@post call.respondText("Error", status = HttpStatusCode.InternalServerError)
            call.respond(HttpStatusCode.Created, transaction)
        }
    }
}