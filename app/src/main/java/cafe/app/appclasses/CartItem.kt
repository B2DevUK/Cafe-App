package cafe.app.appclasses

data class CartItem(
    val product: Product,
    var quantity: Int,
    val id: Int // Unique identifier for the cart item
)


