package cafe.app.test.appclasses

import cafe.app.appclasses.CardDetails
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class CardDetailsTest {

    @Test
    fun `CardDetails initialization with correct values`() {
        val cardDetails = CardDetails(cardNumber = 1234567812345678L, expiryDate = "12/24", securityNumber = 123, fullName = "John Doe")

        assertEquals(1234567812345678L, cardDetails.cardNumber)
        assertEquals("12/24", cardDetails.expiryDate)
        assertEquals(123, cardDetails.securityNumber)
        assertEquals("John Doe", cardDetails.fullName)
    }

    @Test
    fun `CardDetails initialization with incorrect values`() {
        // Negative card number (invalid)
        val cardDetailsWithNegativeCardNumber = CardDetails(
            cardNumber = -1234567812345678L,
            expiryDate = "12/24",
            securityNumber = 123,
            fullName = "Jane Smith"
        )

        assertNotEquals(-1234567812345678L, cardDetailsWithNegativeCardNumber.cardNumber)
        assertEquals(0L, cardDetailsWithNegativeCardNumber.cardNumber) // Expecting a default value (0) due to invalid card number

        // Empty expiry date (invalid)
        val cardDetailsWithEmptyExpiryDate = CardDetails(
            cardNumber = 9876543210987654L,
            expiryDate = "",
            securityNumber = 456,
            fullName = "Alice Johnson"
        )

        assertEquals(9876543210987654L, cardDetailsWithEmptyExpiryDate.cardNumber)
        assertEquals("", cardDetailsWithEmptyExpiryDate.expiryDate) // Expecting an empty expiry date due to invalid format

        // Negative security number (invalid)
        val cardDetailsWithNegativeSecurityNumber = CardDetails(
            cardNumber = 5555666677778888L,
            expiryDate = "09/23",
            securityNumber = -789,
            fullName = "Bob Brown"
        )

        assertEquals(5555666677778888L, cardDetailsWithNegativeSecurityNumber.cardNumber)
        assertNotEquals(-789, cardDetailsWithNegativeSecurityNumber.securityNumber) // Expecting a non-negative security number

        // Empty full name (invalid)
        val cardDetailsWithEmptyFullName = CardDetails(
            cardNumber = 1111222233334444L,
            expiryDate = "06/22",
            securityNumber = 987,
            fullName = ""
        )

        assertEquals(1111222233334444L, cardDetailsWithEmptyFullName.cardNumber)
        assertEquals("", cardDetailsWithEmptyFullName.fullName) // Expecting an empty full name due to invalid format
    }

    // TODO: ADD MORE VALIDATION FOR CARD DETAILS
}

