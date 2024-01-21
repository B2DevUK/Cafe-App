package cafe.app.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import cafe.app.R
import cafe.app.appclasses.AppUser
import cafe.app.database.UsersDatabaseHelper
import com.google.firebase.auth.FirebaseAuth

class CreateAccount : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_creation_createaccount)

        auth = FirebaseAuth.getInstance()

        findViewById<Button>(R.id.accountCreation_createAccount_createButton).setOnClickListener {
            val email = findViewById<EditText>(R.id.accountCreation_createAccount_emailEntry).text.toString()
            val password = findViewById<EditText>(R.id.accountCreation_createAccount_passwordEntry).text.toString()

            createAccount(email, password)
        }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // User registration successful
                    Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()

                    // Get the user's email
                    val userEmail = task.result?.user?.email

                    // Create an instance of AppUser with the email and an empty string for password
                    val appUser = AppUser(userEmail ?: "", "") // You can set name and password as needed

                    // Save the user to the local database
                    val usersDatabaseHelper = UsersDatabaseHelper(this)
                    val newRowId = usersDatabaseHelper.addUser(appUser)

                    // Check if the user was successfully saved to the database
                    if (newRowId != -1L) {
                        Toast.makeText(this, "User data saved to database", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to save user data to database", Toast.LENGTH_SHORT).show()
                    }

                    // Redirect to Login Page
                    val loginIntent = Intent(this, LoginAccount::class.java)
                    startActivity(loginIntent)
                    finish()
                } else {
                    // User registration failed
                    Toast.makeText(this, "Account creation failed: ${task.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
    }

}
