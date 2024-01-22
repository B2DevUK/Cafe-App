package cafe.app.test.appclasses

import cafe.app.appclasses.OrderDetail
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class OrderDetailTest {

    @Test
    fun `OrderDetail initialization with correct values`() {
        val orderDetail = OrderDetail(
            id = 1,
            orderId = 2,
            productId = 3,
            quantity = 4,
            totalPrice = 5.0
        )

        assertEquals(1, orderDetail.id)
        assertEquals(2, orderDetail.orderId)
        assertEquals(3, orderDetail.productId)
        assertEquals(4, orderDetail.quantity)
        assertEquals(5.0, orderDetail.totalPrice, 0.001)
    }

    @Test
    fun `OrderDetail initialization with incorrect values`() {
        // Negative quantity (invalid)
        val invalidQuantity = -1
        val orderDetailWithInvalidQuantity = OrderDetail(
            id = 2,
            orderId = 3,
            productId = 4,
            quantity = invalidQuantity,
            totalPrice = 5.0
        )

        assertNotEquals(invalidQuantity, orderDetailWithInvalidQuantity.quantity)
        assertEquals(0, orderDetailWithInvalidQuantity.quantity) // Expecting a default value (0) due to an invalid quantity

        // Negative total price (invalid)
        val invalidTotalPrice = -1.0
        val orderDetailWithInvalidTotalPrice = OrderDetail(
            id = 3,
            orderId = 4,
            productId = 5,
            quantity = 6,
            totalPrice = invalidTotalPrice
        )

        assertNotEquals(invalidTotalPrice, orderDetailWithInvalidTotalPrice.totalPrice)
        assertEquals(0.0, orderDetailWithInvalidTotalPrice.totalPrice, 0.001) // Expecting a default value (0.0) due to an invalid total price

        // Zero quantity (valid)
        val validQuantity = 0
        val orderDetailWithZeroQuantity = OrderDetail(
            id = 4,
            orderId = 5,
            productId = 6,
            quantity = validQuantity,
            totalPrice = 7.5
        )

        assertEquals(validQuantity, orderDetailWithZeroQuantity.quantity) // Zero quantity is valid

        // Zero total price (valid)
        val validTotalPrice = 0.0
        val orderDetailWithZeroTotalPrice = OrderDetail(
            id = 5,
            orderId = 6,
            productId = 7,
            quantity = 8,
            totalPrice = validTotalPrice
        )

        assertEquals(validTotalPrice, orderDetailWithZeroTotalPrice.totalPrice, 0.001) // Zero total price is valid
    }

    // TODO: ADD VALIDATION FOR NEGATIVE VALUES
}

