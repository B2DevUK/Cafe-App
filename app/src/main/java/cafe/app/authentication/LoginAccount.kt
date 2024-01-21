package cafe.app.authentication

import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import cafe.app.R
import cafe.app.Dashboard
import cafe.app.database.DBHelper
import com.google.firebase.auth.FirebaseAuth

class LoginAccount : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_creation_login)

        dbHelper = DBHelper(this)

        auth = FirebaseAuth.getInstance()

        findViewById<Button>(R.id.accountCreation_login_loginButton).setOnClickListener {
            val email =
                findViewById<EditText>(R.id.accountCreation_login_emailEntry).text.toString()
            val password =
                findViewById<EditText>(R.id.accountCreation_login_passwordEntry).text.toString()

            loginUser(email, password)
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    // Redirect to Dashboard Activity
                    val adminCheck: Boolean = dbHelper.adminCheck(email)
                    if (adminCheck == true) {
                        val dashboardIntent = Intent(this, Dashboard::class.java)
                        startActivity(dashboardIntent)
                        finish()
                    } else {
                        val dashboardIntent = Intent(this, Dashboard::class.java)
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

    }
