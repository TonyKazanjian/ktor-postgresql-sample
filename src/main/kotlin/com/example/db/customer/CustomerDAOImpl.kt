package com.example.db.customer

import com.example.db.customer.CustomersTable.email
import com.example.db.customer.CustomersTable.firstName
import com.example.db.customer.CustomersTable.lastName
import com.example.db.DatabaseFactory.dbQuery
import com.example.db.transaction.mapToModel
import com.example.models.Customer
import org.jetbrains.exposed.sql.*


class CustomerDAOImpl : CustomerDAO {

    override suspend fun allCustomers(): List<Customer> = dbQuery {
        CustomerEntity
            .all()
            .mapLazy {
                it.mapToModel()
            }.toList()
    }

    override suspend fun customer(id: Int): Customer? = dbQuery {
        CustomerEntity.findById(id)?.mapToModel()
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
        updateEntity(customerToEdit?.firstName, firstName){
            customerToEdit?.firstName = firstName
        }
        updateEntity(customerToEdit?.lastName, lastName){
            customerToEdit?.lastName = lastName
        }
        updateEntity(customerToEdit?.email, email){
            customerToEdit?.email = email
        }
        customerToEdit?.mapToModel()
    }

    override suspend fun deleteCustomer(id: Int): Boolean = dbQuery {
        CustomersTable.deleteWhere { CustomersTable.id eq id } > 0
    }

    private fun updateEntity(oldData: String?, newData: String?, executeUpdate: () -> Unit) {
        if (oldData != newData) {
            executeUpdate()
        }
    }

    private fun mapResultRowToModel(row: ResultRow) =
        Customer(
            id = row[CustomersTable.id].value,
            firstName = row[firstName],
            lastName = row[lastName],
            email = row[email]
        )
}

fun CustomerEntity.mapToModel(): Customer =
    Customer(
        id = this.id.value,
        firstName = this.firstName,
        lastName = this.lastName,
        email = this.email,
        transactions = this.transactions.mapLazy { entity ->
            entity.mapToModel()
        }.toList()
    )


