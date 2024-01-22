@file:Suppress("KDocUnresolvedReference")

package cafe.app.appclasses

/**
 * [CardDetails]
 * Description: Data class representing card details for payment processing.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Properties]
 * - [cardNumber]: The long integer representing the card number (16 Digits).
 * - [expiryDate]: The string representing the card's expiry date in the "mm/yy" format.
 * - [securityNumber]: The integer representing the card's security number (CVC).
 * - [fullName]: The full name associated with the card, formatted with a space between first and last names.
 */

data class CardDetails(
    val cardNumber: Long,
    val expiryDate: String,
    var securityNumber: Int,
    var fullName: String
)

