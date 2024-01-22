@file:Suppress("KDocUnresolvedReference")

package cafe.app.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import cafe.app.R
import cafe.app.Dashboard
import cafe.app.database.DBHelper
import com.google.firebase.auth.FirebaseAuth

/**
 * [LoginAccount]
 * Description: Activity responsible for user login using email and password.
 *
 * [Author]
 * Author Name: Brandon Sharp & Jamie Clarke
 *
 * [Properties]
 * - [auth]: FirebaseAuth instance for Firebase authentication.
 * - [dbHelper]: DBHelper instance for database operations.
 *
 * [Methods]
 * - [onCreate]: Initializes the activity and sets up UI components and click listener.
 * - [loginUser]: Attempts to log in the user using the provided email and password.
 * - [showToast]: Displays a Toast message with the given message text.
 */
class LoginAccount : AppCompatActivity() {

    // Properties
    private lateinit var auth: FirebaseAuth
    private lateinit var dbHelper: DBHelper

    /**
     * [onCreate]
     * Description: Initializes the activity and sets up UI components and click listener.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_creation_login)

        dbHelper = DBHelper(this)

        auth = FirebaseAuth.getInstance()

        // Set up click listener for the login button
        findViewById<Button>(R.id.accountCreation_login_loginButton).setOnClickListener {
            val email =
                findViewById<EditText>(R.id.accountCreation_login_emailEntry).text.toString()
            val password =
                findViewById<EditText>(R.id.accountCreation_login_passwordEntry).text.toString()

            loginUser(email, password)
        }
    }

    /**
     * [loginUser]
     * Description: Attempts to log in the user using the provided email and password.
     * - [email]: User's email address for login.
     * - [password]: User's password for login.
     */
    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    // Redirect to Dashboard Activity
                    val adminCheck: Boolean = dbHelper.adminCheck(email)
                    val dashboardIntent = Intent(this, Dashboard::class.java)
                    if (adminCheck) {
                        startActivity(dashboardIntent)
                        finish()
                    } else {
                        startActivity(dashboardIntent)
                        finish()
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Login failed: ${task.exception?.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
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

