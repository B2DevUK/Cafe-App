package cafe.app.appclasses

data class Order(
    val orderId: Long,
    val customerId: Long,
    val orderItems: List<OrderItem> = emptyList(), // Provide a default empty list
    val orderPrice: Double,
    val orderStatus: Int
)


