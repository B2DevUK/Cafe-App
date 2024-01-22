package cafe.app.test.appclasses

import cafe.app.appclasses.CartItem
import cafe.app.appclasses.Product
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class CartItemTest {

    @Test
    fun `CartItem initialization with correct values`() {
        val product = Product(id = 1, name = "Coffee", price = 2.5, image = null, isAvailable = 1, category = "Beverages")
        val cartItem = CartItem(product, quantity = 2)

        assertEquals(product, cartItem.product)
        assertEquals(2, cartItem.quantity)
    }

    @Test
    fun `CartItem initialization with incorrect values`() {
        // Negative quantity (invalid)
        val invalidQuantity = -1
        val product = Product(id = 2, name = "Tea", price = 1.5, image = null, isAvailable = 1, category = "Beverages")
        val cartItemWithInvalidQuantity = CartItem(product, quantity = invalidQuantity)

        assertNotEquals(invalidQuantity, cartItemWithInvalidQuantity.quantity)
        assertEquals(0, cartItemWithInvalidQuantity.quantity) // Expecting a default value (0) due to an invalid quantity

        // Zero quantity (valid)
        val validQuantity = 0
        val cartItemWithZeroQuantity = CartItem(product, quantity = validQuantity)

        assertEquals(product, cartItemWithZeroQuantity.product)
        assertEquals(validQuantity, cartItemWithZeroQuantity.quantity) // Zero quantity is valid
    }

    // TODO: ADD MORE VALIDATION
}

