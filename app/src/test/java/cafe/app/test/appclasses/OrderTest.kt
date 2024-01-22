package cafe.app.test.appclasses

import cafe.app.appclasses.Order
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class OrderTest {

    @Test
    fun `Order initialization with correct values`() {
        val order = Order(
            id = 1,
            customerId = 2,
            date = "2022-01-01",
            time = "12:00:00",
            status = 1
        )

        assertEquals(1, order.id)
        assertEquals(2, order.customerId)
        assertEquals("2022-01-01", order.date)
        assertEquals("12:00:00", order.time)
        assertEquals(1, order.status)
    }

    @Test
    fun `Order initialization with incorrect values`() {
        // Negative customer ID (invalid)
        val invalidCustomerId = -1
        val orderWithInvalidCustomerId = Order(
            id = 2,
            customerId = invalidCustomerId,
            date = "2022-01-02",
            time = "13:00:00",
            status = 1
        )

        assertNotEquals(invalidCustomerId, orderWithInvalidCustomerId.customerId)
        assertEquals(0, orderWithInvalidCustomerId.customerId) // Expecting a default value (0) due to an invalid customer ID

        // Empty date (invalid)
        val emptyDate = ""
        val orderWithEmptyDate = Order(
            id = 3,
            customerId = 3,
            date = emptyDate,
            time = "14:00:00",
            status = 1
        )

        assertNotEquals(emptyDate, orderWithEmptyDate.date)
        assertEquals("", orderWithEmptyDate.date) // Expecting an empty string due to an invalid date

        // Negative status (invalid)
        val invalidStatus = -1
        val orderWithInvalidStatus = Order(
            id = 4,
            customerId = 4,
            date = "2022-01-04",
            time = "15:00:00",
            status = invalidStatus
        )

        assertNotEquals(invalidStatus, orderWithInvalidStatus.status)
        assertEquals(0, orderWithInvalidStatus.status) // Expecting a default value (0) due to an invalid status
    }

    // TODO: Add more data validation for negative values
}


