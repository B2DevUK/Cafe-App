@file:Suppress("KDocUnresolvedReference")

package cafe.app.ui.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cafe.app.R
import cafe.app.adapters.CheckoutAdapter
import cafe.app.appclasses.CardDetails
import cafe.app.database.DBHelper
import cafe.app.databinding.FragmentCheckoutBinding
import cafe.app.appclasses.CartItem
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

/**
 * [CheckoutFragment]
 * Description: Fragment responsible for handling the checkout process, displaying cart items, and processing orders.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Properties]
 * - [checkoutViewModel]: ViewModel for managing the checkout process.
 * - [checkoutAdapter]: Adapter for the RecyclerView displaying cart items.
 * - [binding]: View binding object for the fragment's layout.
 * - [dbHelper]: Database helper for database operations.
 * - [user]: Current Firebase user.
 * - [customerId]: ID of the current customer.
 *
 * [Methods]
 * - [onCreateView]: Initializes the fragment's view and sets up necessary components.
 * - [setupRecyclerView]: Initializes and configures the RecyclerView to display cart items.
 * - [setupPaymentMethodSelection]: Sets up the payment method selection and defaults to "pay by cash."
 * - [showCardDetailsDialog]: Displays a dialog for entering card details if "pay by card" is selected.
 * - [collectCardDetails]: Collects card details entered by the user.
 * - [createOrderFromCart]: Creates an order from the cart items and records the payment in the database.
 * - [updateTotalPrice]: Updates the total price displayed based on the cart items.
 * - [showOrderConfirmation]: Displays a toast message for a successful order placement and resets the payment method to "pay by cash."
 * - [showError]: Displays a toast message for order placement failure.
 */
class CheckoutFragment : Fragment() {

    private lateinit var checkoutViewModel: CheckoutViewModel
    private lateinit var checkoutAdapter: CheckoutAdapter
    private lateinit var binding: FragmentCheckoutBinding
    private lateinit var dbHelper: DBHelper

    private val user = FirebaseAuth.getInstance().currentUser
    private val customerId = user?.uid

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        dbHelper = DBHelper(requireContext())

        checkoutViewModel = ViewModelProvider(requireActivity())[CheckoutViewModel::class.java]
        setupRecyclerView()
        setupPaymentMethodSelection()

        checkoutViewModel.cartItems.observe(viewLifecycleOwner) { cartItemsMap ->
            val cartItemsList = cartItemsMap.values.toList()
            checkoutAdapter.submitList(cartItemsList)
            updateTotalPrice(cartItemsMap)
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        checkoutAdapter = CheckoutAdapter(requireContext(),
            incrementClickListener = { cartItem ->
                checkoutViewModel.incrementCartItem(cartItem.product)
            },
            decrementClickListener = { cartItem ->
                checkoutViewModel.decrementCartItem(cartItem.product)
            },
            removeClickListener = { cartItem ->
                checkoutViewModel.removeFromCart(cartItem.product)
            }
        )
        binding.checkoutRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = checkoutAdapter
        }
    }

    private fun setupPaymentMethodSelection() {
        binding.payByCashRadioButton.isChecked = true

        binding.paymentMethodRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.payByCardRadioButton) {
                showCardDetailsDialog()
            }
        }
    }

    private fun showCardDetailsDialog() {
        val dialogFragment = CardDetailsDialogFragment()
        dialogFragment.setCardDetailsListener(object : CardDetailsDialogFragment.CardDetailsListener {
            override fun onCardDetailsEntered(cardDetails: CardDetails) {
                Toast.makeText(context, "Card details entered: ${cardDetails.cardNumber}", Toast.LENGTH_SHORT).show()
                createOrderFromCart(checkoutViewModel.cartItems.value ?: emptyMap())
            }
        })
        dialogFragment.show(parentFragmentManager, "CardDetailsDialog")
    }

    private fun collectCardDetails(): CardDetails? {
        val cardNumber = binding.cardNumberEditText.text.toString().trim()
        val expiryDate = binding.cardExpiryDateEditText.text.toString().trim()
        val securityNumber = binding.cardSecurityNumberEditText.text.toString().trim()
        val fullName = binding.cardFullNameEditText.text.toString().trim()

        if (cardNumber.isEmpty() || expiryDate.isEmpty() || securityNumber.isEmpty() || fullName.isEmpty()) {
            return null
        }

        return CardDetails(cardNumber.toLong(), expiryDate, securityNumber.toInt(), fullName)
    }

    private fun createOrderFromCart(cartItems: Map<Int, CartItem>) {
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        val customerIdInt = dbHelper.getCustomerIdByFirebaseUid(customerId ?: "")
        if (customerIdInt != null && customerIdInt > 0) {
            val orderId = dbHelper.addOrder(customerIdInt, currentDate, currentTime, 1) // Pending Order Status
            if (orderId > 0) {
                var totalAmount = 0.0
                cartItems.values.forEach { cartItem ->
                    val orderDetailId = dbHelper.addOrderDetail(orderId.toInt(), cartItem.product.id, cartItem.quantity, cartItem.quantity * cartItem.product.price)
                    if (orderDetailId > 0) {
                        totalAmount += cartItem.quantity * cartItem.product.price
                    }
                }

                val paymentMethod = if (binding.payByCardRadioButton.isChecked) "card" else "cash"
                dbHelper.addPayment(orderId.toInt(), paymentMethod, totalAmount, currentDate)

                checkoutViewModel.clearCart()
                showOrderConfirmation()
            } else {
                showError()
            }
        }
    }

    private fun updateTotalPrice(cartItems: Map<Int, CartItem>) {
        val totalPrice = cartItems.values.sumOf { it.quantity * it.product.price }
        binding.totalPriceTextView.text = getString(R.string.total_price, totalPrice)
    }

    private fun showOrderConfirmation() {
        Toast.makeText(context, "Order placed successfully! Head to your notifications tab to track your order.", Toast.LENGTH_SHORT).show()
        binding.payByCashRadioButton.isChecked = true
    }

    private fun showError() {
        Toast.makeText(context, "Failed to place the order. Please try again.", Toast.LENGTH_SHORT).show()
    }
}

