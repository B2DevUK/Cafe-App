@file:Suppress("KDocUnresolvedReference")

package cafe.app.ui.accountsettings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cafe.app.databinding.FragmentAccountSettingsBinding
import com.google.firebase.auth.FirebaseAuth

/**
 * [AccountSettingsFragment]
 * Description: Fragment responsible for displaying and updating user account settings.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Properties]
 * - [binding]: The binding object for the fragment's layout.
 * - [viewModel]: The ViewModel associated with this fragment.
 *
 * [Methods]
 * - [onCreateView]: Initializes the fragment's view, sets up UI components, and binds data.
 * - [loadUserInfo]: Loads and displays the current user's information in the UI.
 * - [updateUserInfo]: Updates the user's information in Firebase and the ViewModel.
 * - [showToast]: Displays a Toast message with the given message text.
 */
class AccountSettingsFragment : Fragment() {

    // Properties
    private lateinit var binding: FragmentAccountSettingsBinding
    private lateinit var viewModel: AccountSettingsViewModel

    /**
     * [onCreateView]
     * Description: Initializes the fragment's view, sets up UI components, and binds data.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAccountSettingsBinding.inflate(inflater, container, false)

        // Initialize ViewModel with a factory
        val factory = AccountSettingsViewModelFactory(requireActivity().applicationContext)
        viewModel = ViewModelProvider(this, factory)[AccountSettingsViewModel::class.java]

        // Load user information and set up UI components
        loadUserInfo()

        // Set up click listener for the "Update Info" button
        binding.buttonUpdateInfo.setOnClickListener {
            updateUserInfo()
        }

        return binding.root
    }

    /**
     * [loadUserInfo]
     * Description: Loads and displays the current user's information in the UI.
     */
    private fun loadUserInfo() {
        viewModel.loadCurrentUser()
        viewModel.currentUser.observe(viewLifecycleOwner) { user ->
            user?.let {
                // Populate UI fields with user information
                binding.editTextFullName.setText(it.fullName.ifEmpty { "Change Me" })
                binding.editTextEmail.setText(it.email)
                binding.editTextPhoneNo.setText(it.phoneNo.ifEmpty { "Change Me" })
                binding.editTextUserName.setText(it.userName)
                binding.editTextPassword.setText("********")
            }
        }
    }

    /**
     * [updateUserInfo]
     * Description: Updates the user's information in Firebase and the ViewModel.
     */
    private fun updateUserInfo() {
        val firebaseUid = FirebaseAuth.getInstance().currentUser?.uid

        firebaseUid?.let { uid ->
            val fullName = binding.editTextFullName.text.toString()
            val email = binding.editTextEmail.text.toString()
            val phoneNo = binding.editTextPhoneNo.text.toString().takeIf { it != "Change Me" } ?: ""
            val userName = binding.editTextUserName.text.toString()
            val newPassword = binding.editTextPassword.text.toString().takeIf { it != "********" }

            // Update email if changed
            if (email != null && email != FirebaseAuth.getInstance().currentUser?.email) {
                FirebaseAuth.getInstance().currentUser?.updateEmail(email)
                    ?.addOnCompleteListener { emailUpdateTask ->
                        if (emailUpdateTask.isSuccessful) {
                            // Email updated successfully
                            showToast("Email updated successfully")
                        } else {
                            // Email update failed
                            showToast("Failed to update email")
                        }
                    }
            }

            // Update password if changed
            newPassword?.let { password ->
                FirebaseAuth.getInstance().currentUser?.updatePassword(password)
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            showToast("Password updated successfully")
                        } else {
                            showToast("Failed to update password")
                        }
                    }
            }

            // Update user details in ViewModel and display success message
            viewModel.updateUserDetails(uid, fullName, email, phoneNo, userName, newPassword).also {
                loadUserInfo()
                showToast("Details updated successfully")
            }

        } ?: showToast("Error: User ID is unavailable")
    }

    /**
     * [showToast]
     * Description: Displays a Toast message with the given message text.
     * - [message]: The message to be displayed in the Toast.
     */
    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

