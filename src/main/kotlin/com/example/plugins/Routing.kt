package com.example.plugins

import com.example.db.CustomerDAO
import com.example.routes.customerRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(dao: CustomerDAO) {
    routing {
        customerRouting(dao)
    }
}
