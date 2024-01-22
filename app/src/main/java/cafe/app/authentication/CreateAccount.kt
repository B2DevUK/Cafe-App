@file:Suppress("KDocUnresolvedReference")

package cafe.app.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import cafe.app.R
import cafe.app.database.DBHelper
import com.google.firebase.auth.FirebaseAuth

/**
 * [CreateAccount]
 * Description: Activity responsible for creating a user account with email and password.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Properties]
 * - [auth]: FirebaseAuth instance for Firebase authentication.
 * - [dbHelper]: DBHelper instance for database operations.
 *
 * [Methods]
 * - [onCreate]: Initializes the activity and sets up UI components and click listener.
 * - [createAccount]: Creates a user account using the provided email and password.
 * - [showToast]: Displays a Toast message with the given message text.
 */
class CreateAccount : AppCompatActivity() {

    // Properties
    private lateinit var auth: FirebaseAuth
    private lateinit var dbHelper: DBHelper

    /**
     * [onCreate]
     * Description: Initializes the activity and sets up UI components and click listener.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_creation_createaccount)

        auth = FirebaseAuth.getInstance()
        dbHelper = DBHelper(this)

        // Set up click listener for the create account button
        findViewById<Button>(R.id.accountCreation_createAccount_createButton).setOnClickListener {
            val userName = findViewById<EditText>(R.id.accountCreation_createAccount_usernameEntry).text.toString().trim()
            val email = findViewById<EditText>(R.id.accountCreation_createAccount_emailEntry).text.toString().trim()
            val password = findViewById<EditText>(R.id.accountCreation_createAccount_passwordEntry).text.toString().trim()

            createAccount(email, password, userName)
        }
    }

    /**
     * [createAccount]
     * Description: Creates a user account using the provided email and password.
     * - [email]: User's email address for account creation.
     * - [password]: User's chosen password for account creation.
     * - [userName]: User's chosen username for account creation.
     */
    private fun createAccount(email: String, password: String, userName: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val firebaseUid = auth.currentUser?.uid ?: ""
                    Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()

                    // Add customer data to the database
                    val newRowId = dbHelper.addCustomer("", email, "", userName, password, 1, firebaseUid)

                    if (newRowId != -1L) {
                        Toast.makeText(this, "Customer data saved to database", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to save customer data to database", Toast.LENGTH_SHORT).show()
                    }

                    // Redirect to the login page after successful account creation
                    val loginIntent = Intent(this, LoginAccount::class.java)
                    startActivity(loginIntent)
                    finish()
                } else {
                    Toast.makeText(this, "Account creation failed: ${task.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    /**
     * [showToast]
     * Description: Displays a Toast message with the given message text.
     * - [message]: The message to be displayed in the Toast.
     */
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

