package cafe.app.ui.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cafe.app.appclasses.Product
import cafe.app.appclasses.CartItem

class CheckoutViewModel : ViewModel() {
    private val _cartItems = MutableLiveData<MutableMap<Int, CartItem>>()
    val cartItems: LiveData<MutableMap<Int, CartItem>> = _cartItems

    init {
        _cartItems.value = mutableMapOf()
    }

    // Add a product to the cart
    fun addToCart(product: Product) {
        val currentItems = _cartItems.value ?: mutableMapOf()
        currentItems[product.id]?.let {
            it.quantity += 1
        } ?: run {
            currentItems[product.id] = CartItem(product, 1)
        }
        _cartItems.value = currentItems
    }

    // Remove a product from the cart
    fun removeFromCart(product: Product) {
        val currentCart = _cartItems.value ?: mutableMapOf()
        currentCart.remove(product.id)
        _cartItems.value = currentCart
    }

    // Increment the quantity of a cart item
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

    // Decrement the quantity of a cart item
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

    // Clear the cart
    fun clearCart() {
        _cartItems.value?.clear()
        _cartItems.value = _cartItems.value
    }

}
