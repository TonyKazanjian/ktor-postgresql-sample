import com.example.db.transaction.TransactionDao
import com.example.models.Customer
import com.example.models.Receipt
import com.example.models.Transaction

class TransactionDAOFake: TransactionDao {

    val transactionList = mutableListOf<Transaction>()
    override suspend fun addTransactionToCustomer(customerId: Int, item: String, price: Float): Transaction? {
        val index = transactionList.lastIndex
        val transaction = Transaction(index.inc(), item, price, 10L)
        transactionList.add(transaction)
        return transactionList.last()
    }

    override suspend fun getReceiptForCustomer(customerId: Int): Receipt? {
        return Receipt(
            customer = Customer(
                id = customerId,
                firstName = "Rick",
                lastName = "Sanchez",
                email = "rick@gmail.com"
            ),
            transactions = transactionList,
            total = transactionList.map { it.price }.sum()
        )
    }
}