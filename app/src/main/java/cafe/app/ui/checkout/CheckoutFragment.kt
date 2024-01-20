package cafe.app.ui.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cafe.app.R
import cafe.app.adapters.CheckoutAdapter
import cafe.app.appclasses.CartItem
import cafe.app.databinding.FragmentCheckoutBinding

class CheckoutFragment : Fragment() {

    private lateinit var checkoutViewModel: CheckoutViewModel
    private lateinit var checkoutAdapter: CheckoutAdapter // You'll need to create this adapter

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

        checkoutViewModel.cartItems.observe(viewLifecycleOwner) { cartItems ->
            checkoutAdapter.submitList(cartItems)
        }

        return root
    }
}
