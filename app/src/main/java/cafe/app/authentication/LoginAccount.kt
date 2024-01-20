package cafe.app.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import cafe.app.R
import cafe.app.Dashboard
import com.google.firebase.auth.FirebaseAuth

class LoginAccount : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_creation_login)

        auth = FirebaseAuth.getInstance()

        findViewById<Button>(R.id.accountCreation_login_loginButton).setOnClickListener {
            val email = findViewById<EditText>(R.id.accountCreation_login_emailEntry).text.toString()
            val password = findViewById<EditText>(R.id.accountCreation_login_passwordEntry).text.toString()

            loginUser(email, password)
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    // Redirect to Dashboard Activity
                    val dashboardIntent = Intent(this, Dashboard::class.java)
                    startActivity(dashboardIntent)
                    finish()
                } else {
                    Toast.makeText(this, "Login failed: ${task.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
