@file:Suppress("KDocUnresolvedReference")

package cafe.app.appclasses

/**
 * [Admin]
 * Description: Data class representing an admin user.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Properties]
 * - [id]: Unique identifier for the admin.
 * - [fullName]: Full name of the admin.
 * - [email]: Email address of the admin.
 * - [phoneNo]: Phone number of the admin.
 * - [userName]: Username of the admin.
 * - [password]: Password of the admin.
 * - [isActive]: Indicator of whether the admin account is active (1 for active, 0 for inactive).
 */

data class Admin(
    val id: Int,
    val fullName: String,
    val email: String,
    val phoneNo: String,
    val userName: String,
    val password: String,
    val isActive: Int
)

