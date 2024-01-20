package cafe.app.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import cafe.app.R
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
                    Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()
                    // Redirect to Login Page
                    val loginIntent = Intent(this, LoginAccount::class.java)
                    startActivity(loginIntent)
                    finish()
                } else {
                    Toast.makeText(this, "Account creation failed: ${task.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
