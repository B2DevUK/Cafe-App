package cafe.app.ui.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cafe.app.appclasses.CartItem

class CheckoutViewModel : ViewModel() {

    private val _cartItems = MutableLiveData<List<CartItem>>()
    val cartItems: LiveData<List<CartItem>> = _cartItems

    // Initialize the cart as an empty list
    init {
        _cartItems.value = emptyList()
    }

    // Add a product to the cart
    fun addToCart(item: CartItem) {
        val currentCart = _cartItems.value?.toMutableList() ?: mutableListOf()
        currentCart.add(item)
        _cartItems.value = currentCart
    }

    // Remove a product from the cart
    fun removeFromCart(item: CartItem) {
        val currentCart = _cartItems.value?.toMutableList() ?: mutableListOf()
        currentCart.remove(item)
        _cartItems.value = currentCart
    }

    // Update the quantity of a product in the cart
    fun updateCartItem(item: CartItem) {
        val currentCart = _cartItems.value?.toMutableList() ?: mutableListOf()
        val index = currentCart.indexOfFirst { it.id == item.id }
        if (index != -1) {
            currentCart[index] = item
        }
        _cartItems.value = currentCart
    }

    // Increment the quantity of a cart item
    fun incrementCartItem(cartItem: CartItem) {
        val currentCart = _cartItems.value?.toMutableList() ?: mutableListOf()
        val index = currentCart.indexOf(cartItem)
        if (index != -1) {
            currentCart[index] = cartItem.copy(quantity = cartItem.quantity + 1)
            _cartItems.value = currentCart
        }
    }

    // Decrement the quantity of a cart item
    fun decrementCartItem(cartItem: CartItem) {
        val currentCart = _cartItems.value?.toMutableList() ?: mutableListOf()
        val index = currentCart.indexOf(cartItem)
        if (index != -1) {
            if (cartItem.quantity > 1) {
                currentCart[index] = cartItem.copy(quantity = cartItem.quantity - 1)
            } else {
                currentCart.removeAt(index)
            }
            _cartItems.value = currentCart
        }
    }
}
