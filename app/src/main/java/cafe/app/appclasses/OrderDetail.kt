package cafe.app.appclasses

data class OrderDetail(
    val id: Int,
    val orderId: Int,
    val productId: Int,
    val quantity: Int,
    val totalPrice: Double,
)
