package cafe.app.ui.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import cafe.app.R
import cafe.app.appclasses.CardDetails

class CardDetailsDialogFragment : DialogFragment() {
    interface CardDetailsListener {
        fun onCardDetailsEntered(cardDetails: CardDetails)
    }

    private var listener: CardDetailsListener? = null

    fun setCardDetailsListener(listener: CardDetailsListener) {
        this.listener = listener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_card_details_dialog, container, false)

        val cardNumberEditText: EditText = view.findViewById(R.id.cardNumberEditText)
        val cardExpiryDateEditText: EditText = view.findViewById(R.id.cardExpiryDateEditText)
        val cardSecurityNumberEditText: EditText = view.findViewById(R.id.cardSecurityNumberEditText)
        val cardFullNameEditText: EditText = view.findViewById(R.id.cardFullNameEditText)
        val confirmButton: Button = view.findViewById(R.id.confirmButton)
        val cancelButton: Button = view.findViewById(R.id.cancelButton)

        confirmButton.setOnClickListener {
            val cardNumber = cardNumberEditText.text.toString()
            val expiryDate = cardExpiryDateEditText.text.toString()
            val securityNumber = cardSecurityNumberEditText.text.toString()
            val fullName = cardFullNameEditText.text.toString()

            if (isValidCardDetails(cardNumber, expiryDate, securityNumber, fullName)) {
                listener?.onCardDetailsEntered(CardDetails(cardNumber.toLong(), expiryDate, securityNumber.toInt(), fullName))
                dismiss()
            } else {
                Toast.makeText(requireContext(), "Invalid card details", Toast.LENGTH_SHORT).show()
            }
        }

        cancelButton.setOnClickListener { dismiss() }

        return view
    }

    // Function to validate card details (customize as needed)
    private fun isValidCardDetails(
        cardNumber: String,
        expiryDate: String,
        securityNumber: String,
        fullName: String
    ): Boolean {
        // Validate card number (must be 16 digits long)
        if (cardNumber.length != 16 || !cardNumber.matches(Regex("\\d{16}"))) {
            showToast("Invalid card number. It must be 16 digits long.")
            return false
        }

        // Validate expiry date (must be in mm/yy format)
        val expiryDateRegex = Regex("\\d{2}/\\d{2}")
        if (!expiryDate.matches(expiryDateRegex)) {
            showToast("Invalid expiry date. It must be in mm/yy format.")
            return false
        }

        // Validate security number (must be 3 digits)
        if (securityNumber.length != 3 || !securityNumber.matches(Regex("\\d{3}"))) {
            showToast("Invalid security number. It must be 3 digits long.")
            return false
        }

        // Validate full name (must be in the format "First Last" with capital letters)
        val nameRegex = Regex("^[A-Z][a-z]+ [A-Z][a-z]+")
        if (!fullName.matches(nameRegex)) {
            showToast("Invalid full name. It must be in the format 'First Last' with capital letters.")
            return false
        }

        // All validations passed, card details are valid
        return true
    }

    // Helper function to show a toast message
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}

