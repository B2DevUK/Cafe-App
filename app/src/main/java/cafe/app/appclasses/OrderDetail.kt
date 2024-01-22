@file:Suppress("KDocUnresolvedReference")

package cafe.app.appclasses

/**
 * [OrderDetail]
 * Description: Data class representing the details of a product within an order.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Properties]
 * - [id]: The unique identifier for the order detail (auto-incremented).
 * - [orderId]: The identifier of the order to which this detail belongs.
 * - [productId]: The identifier of the product associated with this detail.
 * - [quantity]: The quantity of the product in the order.
 * - [totalPrice]: The total price of the product quantity.
 */

data class OrderDetail(
    val id: Int = 0, // Autoincrement
    val orderId: Int,
    val productId: Int,
    val quantity: Int,
    val totalPrice: Double,
)
