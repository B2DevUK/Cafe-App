package cafe.app.test.appclasses

import cafe.app.appclasses.Customer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class CustomerTest {

    @Test
    fun customerInitializationWithCorrectValues() {
        val customer = Customer(
            id = 1,
            fullName = "John Doe",
            email = "john@example.com",
            phoneNo = "1234567890",
            userName = "johnD",
            password = "secret",
            isActive = 1,
            firebaseUid = "uid123"
        )

        assertEquals(1, customer.id)
        assertEquals("John Doe", customer.fullName)
        assertEquals("john@example.com", customer.email)
        assertEquals("1234567890", customer.phoneNo)
        assertEquals("johnD", customer.userName)
        assertEquals("secret", customer.password)
        assertEquals(1, customer.isActive)
        assertEquals("uid123", customer.firebaseUid)
    }

    @Test
    fun customerInitializationWithIncorrectValues() {
        // Incorrect email format
        val customerWithInvalidEmail = Customer(
            id = 2,
            fullName = "Jane Smith",
            email = "invalid_email",
            phoneNo = "9876543210",
            userName = "janeS",
            password = "p@ssw0rd",
            isActive = 1,
            firebaseUid = "uid456"
        )

        assertEquals(2, customerWithInvalidEmail.id)
        assertNotEquals("invalid_email", customerWithInvalidEmail.email)
        assertEquals("", customerWithInvalidEmail.email)
        assertEquals("Jane Smith", customerWithInvalidEmail.fullName)
        assertEquals("9876543210", customerWithInvalidEmail.phoneNo)
        assertEquals("janeS", customerWithInvalidEmail.userName)
        assertEquals("p@ssw0rd", customerWithInvalidEmail.password)
        assertEquals(1, customerWithInvalidEmail.isActive)
        assertEquals("uid456", customerWithInvalidEmail.firebaseUid)

        val customerWithEmptyPassword = Customer(
            id = 3,
            fullName = "Alice Johnson",
            email = "alice@example.com",
            phoneNo = "5555555555",
            userName = "aliceJ",
            password = "",
            isActive = 1,
            firebaseUid = "uid789"
        )

        assertEquals(3, customerWithEmptyPassword.id)
        assertEquals("Alice Johnson", customerWithEmptyPassword.fullName)
        assertEquals("alice@example.com", customerWithEmptyPassword.email)
        assertEquals("5555555555", customerWithEmptyPassword.phoneNo)
        assertEquals("aliceJ", customerWithEmptyPassword.userName)
        assertNotEquals("", customerWithEmptyPassword.password)
        assertEquals(1, customerWithEmptyPassword.isActive)
        assertEquals("uid789", customerWithEmptyPassword.firebaseUid)
    }
}



