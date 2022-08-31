import com.example.models.Customer
import com.example.models.Receipt
import com.example.models.Transaction
import com.example.routes.transactionRouting
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import junit.framework.Assert.assertEquals
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import org.junit.Test

class TransactionRoutesTest {

    private val dao = TransactionDAOFake()

    private fun ApplicationTestBuilder.setupTestConfig() {
        environment {
            config = ApplicationConfig("test-application.conf")
        }
        routing {
            transactionRouting(dao)
        }
    }

    @Test
    fun testAddTransactionToCustomer() = testApplication {
        setupTestConfig()
        val transaction = Transaction(
            id = 0,
            item = "Burger",
            price = 12.50f,
            timestamp = 10L
        )
        val response = client.post("/transaction/1"){
            contentType(ContentType.Application.Json)
            val json = Json.encodeToJsonElement(transaction).toString()
            setBody(json)
        }
        assert(dao.transactionList.contains(transaction))
        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun testGetReceiptForCustomer() = testApplication {
        setupTestConfig()
        val transactionList = dao.transactionList.apply {
            addAll(listOf(
                Transaction(
                    id = 0,
                    item = "Burger",
                    price = 12.50f,
                    timestamp = 10L
                ),
                Transaction(
                    id = 1,
                    item = "fries",
                    price = 5.00f,
                    timestamp = 10L
                )
            ))
        }
        val receipt = Receipt(
                customer = Customer(
                    id = 1,
                    firstName = "Rick",
                    lastName = "Sanchez",
                    email = "rick@gmail.com"
                ),
            transactions = transactionList,
            total = 17.50f
        )

        val response = client.get("/transaction/receipt/1") {
            contentType(ContentType.Application.Json)
        }
        assertEquals(HttpStatusCode.OK, response.status)
    }
}