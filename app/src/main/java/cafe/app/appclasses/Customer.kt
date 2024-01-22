@file:Suppress("KDocUnresolvedReference")

package cafe.app.appclasses

/**
 * [Customer]
 * Description: Data class representing a customer, including customer details and Firebase UID.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Properties]
 * - [id]: The unique identifier for the customer.
 * - [fullName]: The full name of the customer.
 * - [email]: The email address of the customer.
 * - [phoneNo]: The phone number of the customer.
 * - [userName]: The username chosen by the customer.
 * - [password]: The password associated with the customer's account.
 * - [isActive]: An integer indicating the customer's account status.
 * - [firebaseUid]: The Firebase UID associated with the customer's account.
 */

data class Customer(
    val id: Int,
    val fullName: String,
    val email: String,
    val phoneNo: String,
    val userName: String,
    val password: String,
    val isActive: Int,
    val firebaseUid: String
)
