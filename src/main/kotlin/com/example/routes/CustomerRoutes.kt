package com.example.routes

import com.example.db.dao
import com.example.models.Customer
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Route.customerRouting() {
    route("/customer") {
        get {
            val customers = dao.allCustomers()
            if (customers.isNotEmpty()){
                call.respond(customers)
            } else {
                call.respondText("No customers found", status = HttpStatusCode.OK)
            }
        }
        get("{id?}"){
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            val customer = dao.customer(id.toInt()) ?: return@get call.respondText(
                "No customer with id $id",
                status = HttpStatusCode.NotFound
            )
            call.respond(customer)
        }
        post {
            val customer = call.receive<Customer>()
            val newCustomer = dao.addNewCustomer(customer.firstName, customer.lastName, customer.email) ?:
                return@post call.respondText("Error", status = HttpStatusCode.InternalServerError)
            call.respond(HttpStatusCode.Created, newCustomer)
        }

        patch("{id?}"){
            val id = call.parameters["id"] ?: return@patch call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )

            val updateRequest = call.receive<Customer>()
            val updatedCustomer = dao.editCustomer(id.toInt(), updateRequest.firstName, updateRequest.lastName, updateRequest.email) ?:
                return@patch call.respondText("Error", status = HttpStatusCode.InternalServerError)
            call.respond(HttpStatusCode.Accepted, updatedCustomer)
        }

        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (!dao.deleteCustomer(id.toInt())) {
                call.respondText(
                    "No customer with id $id",
                    status = HttpStatusCode.NotFound
                )
            } else {
                call.respondText(text = "Customer deleted", status = HttpStatusCode.Accepted)
            }
        }
    }
}