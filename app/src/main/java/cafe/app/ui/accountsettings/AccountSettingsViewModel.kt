package cafe.app.ui.accountsettings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cafe.app.appclasses.Customer
import cafe.app.database.DBHelper
import com.google.firebase.auth.FirebaseAuth

class AccountSettingsViewModel(private val dbHelper: DBHelper) : ViewModel() {

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    val currentUser: LiveData<Customer?> = MutableLiveData()

    fun loadCurrentUser() {
        val firebaseUid = getCurrentUserId() ?: return
        val user = dbHelper.fetchUserDetailsByFirebaseUid(firebaseUid)
        (currentUser as MutableLiveData).postValue(user)
    }

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    fun getCurrentUser(): Customer? {
        val userId = getCurrentUserId() ?: return null
        return dbHelper.fetchUserDetails(userId)
    }

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
            val updatedLocally = true // Modify this condition based on your logic

            if (updatedLocally) {
                Log.d("AccountSettingsVM", "User details updated successfully in the local database.")
            } else {
                Log.e("AccountSettingsVM", "Failed to update user details in the local database.")
                // Handle the error here, e.g., show a toast message to the user
            }

            // Rest of your code...
        } catch (e: Exception) {
            Log.e("AccountSettingsVM", "Error while updating user details in the local database: ${e.message}", e)
            // Handle the error here, e.g., show a toast message to the user
        }
    }
}

