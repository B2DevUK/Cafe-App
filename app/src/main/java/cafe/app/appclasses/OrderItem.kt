package cafe.app.appclasses

data class OrderItem(
    val productId: Long,  // Product ID included in the order
    val quantity: Int,  // Quantity of the product in the order
    val price: Double  // Total price for this item (product price * quantity)
)

