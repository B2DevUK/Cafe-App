package cafe.app.test.appclasses

import cafe.app.appclasses.Product
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class ProductTest {

    @Test
    fun `Product initialization with correct values`() {
        val product = Product(
            id = 1,
            name = "Latte",
            price = 3.5,
            image = "https://example.com/latte.png",
            isAvailable = 1,
            category = "Beverages"
        )

        assertEquals(1, product.id)
        assertEquals("Latte", product.name)
        assertEquals(3.5, product.price, 0.001)
        assertEquals("https://example.com/latte.png", product.image)
        assertEquals(1, product.isAvailable)
        assertEquals("Beverages", product.category)
    }

    @Test
    fun `Product initialization with incorrect values`() {
        // Negative product ID (invalid)
        val invalidProductId = -1
        val productWithInvalidId = Product(
            id = invalidProductId,
            name = "Invalid Product",
            price = 5.0,
            image = "",
            isAvailable = 1,
            category = "Invalid Category"
        )

        assertNotEquals(invalidProductId, productWithInvalidId.id)
        assertEquals(0, productWithInvalidId.id) // Expecting a default value (0) due to an invalid product ID

        // Empty product name (invalid)
        val emptyName = ""
        val productWithEmptyName = Product(
            id = 2,
            name = emptyName,
            price = 4.0,
            image = "https://example.com/product.png",
            isAvailable = 1,
            category = "Valid Category"
        )

        assertNotEquals(emptyName, productWithEmptyName.name)
        assertEquals("", productWithEmptyName.name) // Expecting an empty string due to an invalid product name

        // Negative price (invalid)
        val invalidPrice = -2.0
        val productWithInvalidPrice = Product(
            id = 3,
            name = "Invalid Price Product",
            price = invalidPrice,
            image = "https://example.com/invalid_price_product.png",
            isAvailable = 1,
            category = "Valid Category"
        )

        assertNotEquals(invalidPrice, productWithInvalidPrice.price, 0.001)
        assertEquals(0.0, productWithInvalidPrice.price, 0.001) // Expecting a default value (0.0) due to an invalid price
    }

    // TODO: Add data validation for negative values 
}

