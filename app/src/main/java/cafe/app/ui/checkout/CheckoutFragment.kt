package cafe.app.ui.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cafe.app.R
import cafe.app.adapters.CheckoutAdapter
import cafe.app.databinding.FragmentCheckoutBinding

class CheckoutFragment : Fragment() {

    private lateinit var checkoutViewModel: CheckoutViewModel
    private lateinit var checkoutAdapter: CheckoutAdapter
    private lateinit var totalPriceTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        val root = binding.root

        checkoutViewModel = ViewModelProvider(requireActivity()).get(CheckoutViewModel::class.java)
        checkoutAdapter = CheckoutAdapter(requireContext())

        val recyclerView: RecyclerView = binding.checkoutRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = checkoutAdapter

        // Initialize totalPriceTextView by finding it in the layout
        totalPriceTextView = binding.totalPriceTextView

        checkoutViewModel.cartItems.observe(viewLifecycleOwner) { cartItems ->
            checkoutAdapter.submitList(cartItems)

            // Calculate the total price and update the TextView
            val totalPrice = checkoutAdapter.calculateTotalPrice()
            totalPriceTextView.text = getString(R.string.total_price, totalPrice)

        }

        return root
    }
}

