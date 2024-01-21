package cafe.app.appclasses

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val image: String?, // NULLABLE
    val isAvailable: Int, // BOOLEAN AS INT (0 FALSE | 1 TRUE)
    val category: String
)
