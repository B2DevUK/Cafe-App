@file:Suppress("KDocUnresolvedReference")

package cafe.app.appclasses

/**
 * [CartItem]
 * Description: Data class representing an item in the shopping cart, including the product and quantity.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Properties]
 * - [product]: The product associated with the cart item.
 * - [quantity]: The quantity of the product in the cart.
 */

data class CartItem(
    val product: Product,
    var quantity: Int
)
