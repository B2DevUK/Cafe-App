package cafe.app.appclasses

data class Payment(
    val id: Int,
    val orderId: Int,
    val type: String,
    val amount: Double,
    val date: String
)
