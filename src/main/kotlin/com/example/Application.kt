package com.example

import com.example.db.CustomerDAO
import com.example.db.CustomerDAOImpl
import com.example.db.DatabaseFactory
import com.example.models.Customer
import com.example.plugins.*
import io.ktor.server.application.*
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    DatabaseFactory.init(environment.config)
    configureSerialization()
    configureRouting(provideDao())
}

fun Application.testModule(){
    configureSerialization()
}

fun provideDao() = CustomerDAOImpl().apply {
    runBlocking {
        if (allCustomers().isEmpty()) {
            addNewCustomer("Leona", "Kazanjian", "leona@gmail.com")
        }
    }
}