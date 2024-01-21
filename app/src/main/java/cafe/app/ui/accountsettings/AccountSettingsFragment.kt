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

class AccountSettingsFragment : Fragment() {

    private lateinit var binding: FragmentAccountSettingsBinding
    private lateinit var viewModel: AccountSettingsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAccountSettingsBinding.inflate(inflater, container, false)

        val factory = AccountSettingsViewModelFactory(requireActivity().applicationContext)
        viewModel = ViewModelProvider(this, factory)[AccountSettingsViewModel::class.java]

        loadUserInfo()

        binding.buttonUpdateInfo.setOnClickListener {
            updateUserInfo()
        }
        return binding.root
    }

    private fun loadUserInfo() {
        viewModel.loadCurrentUser()
        viewModel.currentUser.observe(viewLifecycleOwner) { user ->
            user?.let {
                binding.editTextFullName.setText(it.fullName.ifEmpty { "Change Me" })
                binding.editTextEmail.setText(it.email)
                binding.editTextPhoneNo.setText(it.phoneNo.ifEmpty { "Change Me" })
                binding.editTextUserName.setText(it.userName)
                binding.editTextPassword.setText("********")
            }
        }
    }

    private fun updateUserInfo() {
        val firebaseUid = FirebaseAuth.getInstance().currentUser?.uid
        firebaseUid?.let { uid ->
            val fullName = binding.editTextFullName.text.toString()
            val email = binding.editTextEmail.text.toString()
            val phoneNo = binding.editTextPhoneNo.text.toString().takeIf { it != "Change Me" } ?: ""
            val userName = binding.editTextUserName.text.toString()
            // For password, consider how you want to handle it, given the security implications
            val newPassword = binding.editTextPassword.text.toString().takeIf { it != "********" }

            if (email != null && email != FirebaseAuth.getInstance().currentUser?.email) {
                FirebaseAuth.getInstance().currentUser?.updateEmail(email)
                    ?.addOnCompleteListener { emailUpdateTask ->
                        if (emailUpdateTask.isSuccessful) {
                            // Email updated successfully
                            Toast.makeText(context, "Email updated successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            // Email update failed
                            Toast.makeText(context, "Failed to update email", Toast.LENGTH_SHORT).show()
                        }
                    }
            }

            newPassword?.let { password ->
                FirebaseAuth.getInstance().currentUser?.updatePassword(password)
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "Password updated successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Failed to update password", Toast.LENGTH_SHORT).show()
                        }
                    }
            }

            viewModel.updateUserDetails(uid, fullName, email, phoneNo, userName, newPassword).also {
                loadUserInfo()
                Toast.makeText(context, "Details updated successfully", Toast.LENGTH_SHORT).show()
            }

        }?: Toast.makeText(context, "Error: User ID is unavailable", Toast.LENGTH_SHORT).show()
    }
}
