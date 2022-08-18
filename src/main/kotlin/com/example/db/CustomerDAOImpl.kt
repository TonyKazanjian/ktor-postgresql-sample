package com.example.db

import com.example.db.CustomersTable
import com.example.models.Customer
import org.jetbrains.exposed.sql.selectAll
import com.example.db.DatabaseFactory.dbQuery
import com.example.db.CustomersTable.email
import com.example.db.CustomersTable.firstName
import com.example.db.CustomersTable.id
import com.example.db.CustomersTable.lastName
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*


class CustomerDAOImpl : CustomerDAO {

    private fun mapResultRowToModel(row: ResultRow) =
        Customer(
            id = row[CustomersTable.id].value,
            firstName = row[firstName],
            lastName = row[lastName],
            email = row[email]
        )

    override suspend fun allCustomers(): List<Customer> = dbQuery {
        CustomersTable
            .selectAll()
            .mapLazy { mapResultRowToModel(it) }
            .toList()
    }

    override suspend fun customer(id: Int): Customer? = dbQuery {
        CustomersTable.select {
            CustomersTable.id eq id
        }
            .mapLazy { mapResultRowToModel(it)}
            .singleOrNull()
    }

    override suspend fun addNewCustomer(firstName: String, lastName: String, email: String): Customer? = dbQuery {
        val insertStatement = CustomersTable.insert {
            it[CustomersTable.firstName] = firstName
            it[CustomersTable.lastName] = lastName
            it[CustomersTable.email] = email
        }
        insertStatement.resultedValues?.singleOrNull()?.let { mapResultRowToModel(it) }
    }

    override suspend fun editCustomer(id: Int, firstName: String, lastName: String, email: String): Customer? = dbQuery {
        val customerToEdit = CustomerEntity.findById(id)
        customerToEdit?.firstName = firstName
        customerToEdit?.lastName = lastName
        customerToEdit?.email = email
        Customer(
            id = id,
            firstName = customerToEdit?.firstName ?: firstName,
            lastName = customerToEdit?.lastName ?: lastName,
            email = customerToEdit?.email ?: email
        )
    }

    override suspend fun deleteCustomer(id: Int): Boolean = dbQuery {
        CustomersTable.deleteWhere { CustomersTable.id eq id } > 0
    }
}

val dao: CustomerDAO = CustomerDAOImpl().apply {
    runBlocking {
        if(allCustomers().isEmpty()) {
            addNewCustomer("Leona", "Kazanjian", "leona@gmail.com")
        }
    }
}
