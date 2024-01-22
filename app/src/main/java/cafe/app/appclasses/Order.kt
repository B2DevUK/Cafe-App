@file:Suppress("KDocUnresolvedReference")

package cafe.app.appclasses

/**
 * [Order]
 * Description: Data class representing an order placed by a customer.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Properties]
 * - [id]: The unique identifier for the order.
 * - [customerId]: The unique identifier of the customer who placed the order.
 * - [date]: The date when the order was placed.
 * - [time]: The time when the order was placed.
 * - [status]: An integer indicating the status of the order.
 */

data class Order(
    val id: Int,
    val customerId: Int,
    val date: String,
    val time: String,
    val status: Int
)

