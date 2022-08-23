import com.example.db.CustomerDAO
import com.example.models.Customer

class CustomerDAOFake: CustomerDAO {

    val customerList = mutableListOf<Customer>(
        Customer(
            id = 0,
            firstName = "Rick",
            lastName = "Sanchez",
            email = "rick@citadel.com"
        ),
        Customer(
            id = 1,
            firstName = "Morty",
            lastName = "Smith",
            email = "morty@citadel.com"
        ),
    )

    override suspend fun allCustomers(): List<Customer> {
        return customerList
    }

    override suspend fun customer(id: Int): Customer? {
        return customerList.find { it.id == id }
    }

    override suspend fun editCustomer(id: Int, firstName: String, lastName: String, email: String): Customer? {
        return customerList.find { it.id == id }?.copy(firstName = firstName, lastName = lastName, email = email)
    }

    override suspend fun addNewCustomer(firstName: String, lastName: String, email: String): Customer? {
        val newCustomer = Customer(id = 0, firstName, lastName, email)
        customerList.add(newCustomer)
        return newCustomer
    }

    override suspend fun deleteCustomer(id: Int): Boolean {
        return customerList.remove(customerList.find { it.id == id })
    }
}