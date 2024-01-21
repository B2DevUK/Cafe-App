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

class CreateAccount : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_creation_createaccount)

        auth = FirebaseAuth.getInstance()
        dbHelper = DBHelper(this)

        findViewById<Button>(R.id.accountCreation_createAccount_createButton).setOnClickListener {
            val userName = findViewById<EditText>(R.id.accountCreation_createAccount_usernameEntry).text.toString().trim()
            val email = findViewById<EditText>(R.id.accountCreation_createAccount_emailEntry).text.toString().trim()
            val password = findViewById<EditText>(R.id.accountCreation_createAccount_passwordEntry).text.toString().trim()

            createAccount(email, password, userName)
        }
    }

    private fun createAccount(email: String, password: String, userName: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()

                    // ASSUMING PHONE NUMBER NOT AVAILABLE YET
                    val newRowId = dbHelper.addCustomer("", email, "", userName, password, 1)

                    if (newRowId != -1L) {
                        Toast.makeText(this, "Customer data saved to database", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to save customer data to database", Toast.LENGTH_SHORT).show()
                    }

                    val loginIntent = Intent(this, LoginAccount::class.java)
                    startActivity(loginIntent)
                    finish()
                } else {
                    Toast.makeText(this, "Account creation failed: ${task.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
    }

}
