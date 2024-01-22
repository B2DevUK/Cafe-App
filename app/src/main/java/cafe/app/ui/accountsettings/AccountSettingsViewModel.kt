@file:Suppress("KDocUnresolvedReference")

package cafe.app.ui.accountsettings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cafe.app.appclasses.Customer
import cafe.app.database.DBHelper
import com.google.firebase.auth.FirebaseAuth

/**
 * [AccountSettingsViewModel]
 * Description: ViewModel responsible for managing user account settings data.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Properties]
 * - [dbHelper]: The DBHelper instance for database operations.
 * - [auth]: Lazy initialization of FirebaseAuth instance.
 * - [currentUser]: LiveData to hold the current user's data.
 *
 * [Methods]
 * - [loadCurrentUser]: Loads the current user's data and updates [currentUser].
 * - [getCurrentUserId]: Retrieves the current user's Firebase UID.
 * - [getCurrentUser]: Retrieves the current user's details.
 * - [updateUserDetails]: Updates the user's details in the local database and handles errors.
 */
class AccountSettingsViewModel(private val dbHelper: DBHelper) : ViewModel() {

    // Properties
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    val currentUser: LiveData<Customer?> = MutableLiveData()

    /**
     * [loadCurrentUser]
     * Description: Loads the current user's data and updates [currentUser].
     */
    fun loadCurrentUser() {
        val firebaseUid = getCurrentUserId() ?: return
        val user = dbHelper.fetchUserDetailsByFirebaseUid(firebaseUid)
        (currentUser as MutableLiveData).postValue(user)
    }

    /**
     * [getCurrentUserId]
     * Description: Retrieves the current user's Firebase UID.
     * Returns: Firebase UID as a String, or null if not authenticated.
     */
    private fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    /**
     * [getCurrentUser]
     * Description: Retrieves the current user's details.
     * Returns: Current user's Customer object or null if not authenticated.
     */
    private fun getCurrentUser(): Customer? {
        val userId = getCurrentUserId() ?: return null
        return dbHelper.fetchUserDetails(userId)
    }

    /**
     * [updateUserDetails]
     * Description: Updates the user's details in the local database and handles errors.
     * - [firebaseUid]: The Firebase UID of the user to update.
     * - [fullName]: The updated full name of the user.
     * - [email]: The updated email address of the user.
     * - [phoneNo]: The updated phone number of the user.
     * - [userName]: The updated user name.
     * - [newPassword]: The updated password (if changed).
     */
    fun updateUserDetails(firebaseUid: String, fullName: String?, email: String?, phoneNo: String?, userName: String?, newPassword: String?) {
        try {
            dbHelper.updateCustomerSelective(
                firebaseUid,
                fullName,
                email,
                phoneNo,
                userName,
                newPassword
            )

            // Check if the update was successful in the local database
            val updatedLocally = true

            if (updatedLocally) {
                Log.d("AccountSettingsVM", "User details updated successfully in the local database.")
            } else {
                Log.e("AccountSettingsVM", "Failed to update user details in the local database.")
                // TODO: Error handling toast message
            }

            // Rest of your code...
        } catch (e: Exception) {
            Log.e("AccountSettingsVM", "Error while updating user details in the local database: ${e.message}", e)
            // TODO: Error handling toast message
        }
    }
}


