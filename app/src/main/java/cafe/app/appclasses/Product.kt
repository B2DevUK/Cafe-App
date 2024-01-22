@file:Suppress("KDocUnresolvedReference")

package cafe.app.appclasses

/**
 * [Product]
 * Description: Data class representing a product, including product details such as name, price, and availability.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Properties]
 * - [id]: The unique identifier for the product.
 * - [name]: The name of the product.
 * - [price]: The price of the product.
 * - [image]: The image URL of the product (nullable).
 * - [isAvailable]: An integer indicating product availability (0 for FALSE, 1 for TRUE).
 * - [category]: The category to which the product belongs.
 */
data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val image: String?, // NULLABLE
    val isAvailable: Int, // BOOLEAN AS INT (0 FALSE | 1 TRUE)
    val category: String
)
