@file:Suppress("KDocUnresolvedReference")

package cafe.app.appclasses

/**
 * [Payment]
 * Description: Data class representing a payment made for an order, including payment type and amount.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Properties]
 * - [id]: The unique identifier for the payment.
 * - [orderId]: The identifier of the associated order.
 * - [type]: The type of payment (e.g., "card" or "cash").
 * - [amount]: The payment amount.
 * - [date]: The date when the payment was made.
 */
data class Payment(
    val id: Int,
    val orderId: Int,
    val type: String,
    val amount: Double,
    val date: String
)
