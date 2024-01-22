package cafe.app.test.appclasses

import cafe.app.appclasses.Payment
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class PaymentTest {

    @Test
    fun `Payment initialization with correct values`() {
        val payment = Payment(
            id = 1,
            orderId = 2,
            type = "Card",
            amount = 100.0,
            date = "2022-01-02"
        )

        assertEquals(1, payment.id)
        assertEquals(2, payment.orderId)
        assertEquals("Card", payment.type)
        assertEquals(100.0, payment.amount, 0.001)
        assertEquals("2022-01-02", payment.date)
    }

    @Test
    fun `Payment initialization with incorrect values`() {
        // Negative order ID (invalid)
        val invalidOrderId = -1
        val paymentWithInvalidOrderId = Payment(
            id = 3,
            orderId = invalidOrderId,
            type = "Cash",
            amount = 50.0,
            date = "2022-01-03"
        )

        assertNotEquals(invalidOrderId, paymentWithInvalidOrderId.orderId)
        assertEquals(0, paymentWithInvalidOrderId.orderId) // Expecting a default value (0) due to an invalid order ID

        // Empty payment type (invalid)
        val emptyType = ""
        val paymentWithEmptyType = Payment(
            id = 4,
            orderId = 4,
            type = emptyType,
            amount = 75.0,
            date = "2022-01-04"
        )

        assertNotEquals(emptyType, paymentWithEmptyType.type)
        assertEquals("", paymentWithEmptyType.type) // Expecting an empty string due to an invalid payment type

        // Negative amount (invalid)
        val invalidAmount = -50.0
        val paymentWithInvalidAmount = Payment(
            id = 5,
            orderId = 5,
            type = "Card",
            amount = invalidAmount,
            date = "2022-01-05"
        )

        assertNotEquals(invalidAmount, paymentWithInvalidAmount.amount, 0.001)
        assertEquals(0.0, paymentWithInvalidAmount.amount, 0.001) // Expecting a default value (0.0) due to an invalid amount
    }

    // TODO: Add more data validation for negative values 
}
