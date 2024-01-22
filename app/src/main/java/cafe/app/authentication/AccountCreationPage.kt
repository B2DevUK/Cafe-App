@file:Suppress("KDocUnresolvedReference")

package cafe.app.authentication

import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import cafe.app.R
import cafe.app.Dashboard
import cafe.app.database.DBHelper
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

/**
 * [AccountCreationPage]
 * Description: Activity responsible for handling user authentication and account creation.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Properties]
 * - [googleSignInClient]: GoogleSignInClient instance for handling Google Sign-In.
 * - [auth]: FirebaseAuth instance for Firebase authentication.
 * - [signInResultLauncher]: Activity result launcher for Google Sign-In.
 *
 * [Methods]
 * - [onCreate]: Initializes the activity and sets up UI components and click listeners.
 * - [importProductsFromCSV]: Imports product data from a CSV file into the database.
 * - [importAdminFromCSV]: Imports admin data from a CSV file into the database.
 * - [handleSignInResult]: Handles the result of Google Sign-In, authenticates with Firebase.
 * - [firebaseAuthWithGoogle]: Performs Firebase authentication with the provided Google credentials.
 * - [showToast]: Displays a Toast message with the given message text.
 */
class AccountCreationPage : AppCompatActivity() {

    // Properties
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private val signInResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        handleSignInResult(task)
    }

    /**
     * [onCreate]
     * Description: Initializes the activity and sets up UI components and click listeners.
     * - [gso]: GoogleSignInOptions for configuring Google Sign-In.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_creation_page)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = FirebaseAuth.getInstance()

        // Set up click listeners for buttons
        findViewById<Button>(R.id.accountCreation_loginWithGoogle).setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            signInResultLauncher.launch(signInIntent)
        }

        findViewById<Button>(R.id.accountCreation_loginButton).setOnClickListener {
            val intent = Intent(this, LoginAccount::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.accountCreation_createAccountButton).setOnClickListener {
            val intent = Intent(this, CreateAccount::class.java)
            startActivity(intent)
        }

        // Import data from CSV files into the database
        importAdminFromCSV()
        importProductsFromCSV()
    }

    /**
     * [importProductsFromCSV]
     * Description: Imports product data from a CSV file into the database.
     */
    private fun importProductsFromCSV() {
        val databaseHelper = DBHelper(this)
        databaseHelper.importProductsFromCSV(this)
    }

    /**
     * [importAdminFromCSV]
     *
     * [Author]
     * Author Name: Jamie Clarke
     *
     * Description: Imports admin data from a CSV file into the database.
     */
    private fun importAdminFromCSV() {
        val databaseHelper = DBHelper(this)
        databaseHelper.importAdminFromCSV(this)
    }

    /**
     * [handleSignInResult]
     * Description: Handles the result of Google Sign-In, authenticates with Firebase.
     * - [completedTask]: Task containing the result of Google Sign-In.
     */
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            // Error Handling Toast
            showToast("Sign in failed: ${e.localizedMessage}")
        }
    }

    /**
     * [firebaseAuthWithGoogle]
     * Description: Performs Firebase authentication with the provided Google credentials.
     * - [idToken]: Google ID token obtained from Google Sign-In.
     */
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, Dashboard::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    task.exception?.let {
                        showToast("Authentication failed: ${it.localizedMessage}")
                    }
                }
            }
    }

    /**
     * [showToast]
     * Description: Displays a Toast message with the given message text.
     * - [message]: The message to be displayed in the Toast.
     */
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}

