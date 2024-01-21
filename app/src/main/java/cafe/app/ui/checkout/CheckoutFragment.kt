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
import cafe.app.database.DBHelper
import cafe.app.databinding.FragmentCheckoutBinding
import cafe.app.appclasses.CartItem
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class CheckoutFragment : Fragment() {

    private lateinit var checkoutViewModel: CheckoutViewModel
    private lateinit var checkoutAdapter: CheckoutAdapter
    private lateinit var binding: FragmentCheckoutBinding
    private lateinit var dbHelper: DBHelper

    private val user = FirebaseAuth.getInstance().currentUser
    val customerId = user?.uid


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        dbHelper = DBHelper(requireContext())

        checkoutViewModel = ViewModelProvider(requireActivity())[CheckoutViewModel::class.java]
        setupRecyclerView()
        setupCheckoutButton()

        checkoutViewModel.cartItems.observe(viewLifecycleOwner) { cartItemsMap ->
            val cartItemsList = cartItemsMap.values.toList()
            checkoutAdapter.submitList(cartItemsList)
            updateTotalPrice(cartItemsMap)
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        checkoutAdapter = CheckoutAdapter(requireContext(),
            incrementClickListener = { cartItem: CartItem ->
                checkoutViewModel.incrementCartItem(cartItem.product)
            },
            decrementClickListener = { cartItem: CartItem ->
                checkoutViewModel.decrementCartItem(cartItem.product)
            },
            removeClickListener = { cartItem: CartItem ->
                checkoutViewModel.removeFromCart(cartItem.product)
            }
        )
        binding.checkoutRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = checkoutAdapter
        }
    }

    private fun setupCheckoutButton() {
        binding.checkoutButton.setOnClickListener {
            val cartItems = checkoutViewModel.cartItems.value ?: return@setOnClickListener
            if (cartItems.isEmpty()) {
                Toast.makeText(context, "Cart is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            createOrderFromCart(cartItems, 1)
        }
    }

    private fun createOrderFromCart(cartItems: Map<Int, CartItem>, customerId: Int) {
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        val orderId = if (customerId != null) {
            dbHelper.addOrder(customerId, currentDate, currentTime, 1)
        } else {
            return
        }


        if (orderId > 0) {
            cartItems.values.forEach { cartItem ->
                dbHelper.addOrderDetail(orderId.toInt(), cartItem.product.id, cartItem.quantity, cartItem.quantity * cartItem.product.price)
            }
            checkoutViewModel.clearCart()
            showOrderConfirmation()
        } else {
            showError()
        }
    }

    private fun updateTotalPrice(cartItems: Map<Int, CartItem>) {
        val totalPrice = cartItems.values.sumOf { it.quantity * it.product.price }
        binding.totalPriceTextView.text = getString(R.string.total_price, totalPrice)
    }

    private fun showOrderConfirmation() {
        Toast.makeText(context, "Order placed successfully!", Toast.LENGTH_SHORT).show()
        // Navigate away or refresh the fragment to clear the cart view
    }

    private fun showError() {
        Toast.makeText(context, "Failed to place the order. Please try again.", Toast.LENGTH_SHORT).show()
    }
}
