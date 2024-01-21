package cafe.app.appclasses

data class Order(
    val id: Int,
    val customerId: Int,
    val date: String,
    val time: String,
    val status: Int
)

