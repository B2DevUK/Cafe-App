@file:Suppress("KDocUnresolvedReference")

package cafe.app.ui.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cafe.app.appclasses.Product
import cafe.app.appclasses.CartItem

/**
 * [CheckoutViewModel]
 * Description: ViewModel responsible for managing the shopping cart and cart item operations.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Properties]
 * - [_cartItems]: MutableLiveData to store the cart items as a mutable map of product IDs to cart items.
 * - [cartItems]: LiveData representing the cart items accessible to external components.
 *
 * [Constructor]
 * Initializes the [_cartItems] LiveData with an empty mutable map.
 *
 * [Methods]
 * - [addToCart]: Adds a product to the cart or increments its quantity if already in the cart.
 * - [removeFromCart]: Removes a product from the cart.
 * - [incrementCartItem]: Increments the quantity of a cart item or adds it to the cart if not present.
 * - [decrementCartItem]: Decrements the quantity of a cart item or removes it from the cart if the quantity becomes zero.
 * - [clearCart]: Clears all items from the cart.
 */
class CheckoutViewModel : ViewModel() {
    private val _cartItems = MutableLiveData<MutableMap<Int, CartItem>>()
    val cartItems: LiveData<MutableMap<Int, CartItem>> = _cartItems

    init {
        _cartItems.value = mutableMapOf()
    }

    fun addToCart(product: Product) {
        val currentItems = _cartItems.value ?: mutableMapOf()
        currentItems[product.id]?.let {
            it.quantity += 1
        } ?: run {
            currentItems[product.id] = CartItem(product, 1)
        }
        _cartItems.value = currentItems
    }

    fun removeFromCart(product: Product) {
        val currentCart = _cartItems.value ?: mutableMapOf()
        currentCart.remove(product.id)
        _cartItems.value = currentCart
    }

    fun incrementCartItem(product: Product) {
        val currentCart = _cartItems.value ?: mutableMapOf()
        currentCart[product.id]?.let {
            it.quantity += 1
            _cartItems.value = currentCart
        } ?: run {
            currentCart[product.id] = CartItem(product, 1)
            _cartItems.value = currentCart
        }
    }

    fun decrementCartItem(product: Product) {
        val currentCart = _cartItems.value ?: mutableMapOf()
        currentCart[product.id]?.let { cartItem ->
            if (cartItem.quantity > 1) {
                cartItem.quantity -= 1
                _cartItems.value = currentCart
            } else {
                currentCart.remove(product.id)
                _cartItems.value = currentCart
            }
        }
    }

    fun clearCart() {
        _cartItems.value?.clear()
        _cartItems.value = _cartItems.value
    }
}
