package cafe.app.appclasses

data class Order(
    val orderId: Long,  // Order ID from the database
    val customerId: Long,  // Customer (user) ID associated with the order
    val productId: Long,  // Product ID included in the order
    val orderPrice: Double,  // Total price of the order
    val orderStatus: Int  // Order status (you can define status codes or use enums)
)

