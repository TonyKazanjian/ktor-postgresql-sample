import com.example.models.Customer
import com.example.routes.customerRouting
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import junit.framework.Assert.assertEquals
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.Test

class CustomerRoutesTest {

    private val dao = CustomerDAOFake()
    private fun ApplicationTestBuilder.setupTestConfig() {
        environment {
            config = ApplicationConfig("test-application.conf")
        }
        routing {
            customerRouting(dao)
        }
    }

    @Test
    fun testGetAllCustomers() = testApplication {
        setupTestConfig()
        val response = client.get("/customer") {
            contentType(ContentType.Application.Json)
        }
        assertEquals(HttpStatusCode.OK, response.status)

        dao.customerList.clear()
        val failResponse = client.get("/customer") {
            contentType(ContentType.Application.Json)
        }
        assertEquals(HttpStatusCode.NotFound, failResponse.status)
    }

    @Test
    fun testPostCustomer() = testApplication {
        setupTestConfig()
        val customer = Customer(0, "Tony", "Kazanjian", "tony@email.com")
        val response = client.post("/customer") {
            contentType(ContentType.Application.Json)
            val json = Json.encodeToJsonElement(customer).toString()
            setBody(json)
        }
        assert(dao.customerList.contains(customer))
        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun testGetCustomerById() = testApplication {
        setupTestConfig()
        val response = client.get("/customer/0"){
            contentType(ContentType.Application.Json)
        }
        assertEquals(HttpStatusCode.OK, response.status)

        val failResponse = client.get("/customer/3"){
            contentType(ContentType.Application.Json)
        }
        assertEquals(HttpStatusCode.NotFound, failResponse.status)

    }

    @Test
    fun testDeleteCustomer() = testApplication {
        setupTestConfig()
        val response = client.delete("/customer/0") {
            contentType(ContentType.Application.Json)
        }
        assertEquals(HttpStatusCode.Accepted, response.status)

        val failResponse = client.delete("/customer/0") {
            contentType(ContentType.Application.Json)
        }
        assertEquals(HttpStatusCode.NotFound, failResponse.status)
    }
}

