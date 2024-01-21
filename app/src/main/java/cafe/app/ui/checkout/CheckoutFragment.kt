package cafe.app.ui.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cafe.app.R
import cafe.app.adapters.CheckoutAdapter
import cafe.app.appclasses.Order
import cafe.app.appclasses.OrderItem
import cafe.app.appclasses.CartItem
import cafe.app.database.OrdersDatabaseHelper
import cafe.app.database.ProductDatabaseHelper
import cafe.app.databinding.FragmentCheckoutBinding

class CheckoutFragment : Fragment() {

    private lateinit var checkoutViewModel: CheckoutViewModel
    private lateinit var checkoutAdapter: CheckoutAdapter
    private lateinit var totalPriceTextView: TextView
    private lateinit var checkoutButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        val root = binding.root

        checkoutViewModel = ViewModelProvider(requireActivity()).get(CheckoutViewModel::class.java)

        totalPriceTextView = binding.totalPriceTextView

        checkoutAdapter = CheckoutAdapter(requireContext(),
            incrementClickListener = { cartItem ->
                checkoutViewModel.incrementCartItem(cartItem)
            },
            decrementClickListener = { cartItem ->
                checkoutViewModel.decrementCartItem(cartItem)
            },
            removeClickListener = { cartItem ->
                checkoutViewModel.removeFromCart(cartItem)
            }
        )

        val recyclerView: RecyclerView = binding.checkoutRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = checkoutAdapter

        checkoutButton = binding.checkoutButton

        checkoutButton.setOnClickListener {
            val cartItems = checkoutAdapter.currentList
            val customerId: Long = 1L // Explicitly define as Long

            val order = createOrderFromCart(cartItems, customerId)

            val dbHelper = OrdersDatabaseHelper(requireContext())
            val orderId = dbHelper.addOrder(order)

            if (orderId > 0) {
                showOrderConfirmation()
            } else {
                showError()
            }
        }


        checkoutViewModel.cartItems.observe(viewLifecycleOwner) { cartItems ->
            checkoutAdapter.submitList(cartItems)

            val totalPrice = checkoutAdapter.calculateTotalPrice()
            totalPriceTextView.text = getString(R.string.total_price, totalPrice)
        }

        return root
    }

    fun mapCartItemsToOrderItems(cartItems: List<CartItem>): List<OrderItem> {
        val orderItems = mutableListOf<OrderItem>()
        val productQuantityMap = mutableMapOf<String, Int>() // Map to store product names and their quantities

        val productsDatabaseHelper = ProductDatabaseHelper(requireContext())

        for (cartItem in cartItems) {
            val productName = cartItem.product.name // Fetch the product name from the cart item

            // Fetch the product ID from the database based on the product name
            val product = productsDatabaseHelper.getProductByName(productName)

            if (product != null) {
                val productId = product.id
                val quantity = cartItem.quantity
                val price = product.price * quantity

                // Check if the product name is already in the map
                if (productQuantityMap.containsKey(productName)) {
                    // If it is, update the quantity by adding the new quantity
                    val updatedQuantity = productQuantityMap[productName]!! + quantity
                    productQuantityMap[productName] = updatedQuantity
                } else {
                    // If it's not, add the product name and quantity to the map
                    productQuantityMap[productName] = quantity
                }
            }
        }

        // Iterate through the map and create OrderItems
        for ((productName, quantity) in productQuantityMap) {
            // Fetch the product from the database based on the product name
            val product = productsDatabaseHelper.getProductByName(productName)

            if (product != null) {
                val productId = product.id.toLong() // Convert to Long
                val price = product.price * quantity

                val orderItem = OrderItem(productId, quantity, price)
                orderItems.add(orderItem)
            }
        }

        return orderItems
    }

    fun createOrderFromCart(cartItems: List<CartItem>, customerId: Long): Order {
        val orderItems = mapCartItemsToOrderItems(cartItems) // Use the existing function to map cart items to order items
        val totalPrice = orderItems.sumOf { it.price }

        val order = Order(
            orderId = 0L, // Ensure this is treated as a Long by appending 'L'
            customerId = customerId, // Now correctly a Long
            orderItems = orderItems,
            orderPrice = totalPrice,
            orderStatus = 1 // Assuming this is a predefined status code, no type issue here
        )
        return order
    }

    private fun showOrderConfirmation() {
        // You can show a confirmation message or navigate to a confirmation page
        // Example: Toast.makeText(requireContext(), "Order placed successfully!", Toast.LENGTH_SHORT).show()
    }

    private fun showError() {
        // You can show an error message to the user
        // Example: Toast.makeText(requireContext(), "Failed to place the order. Please try again.", Toast.LENGTH_SHORT).show()
    }
}
