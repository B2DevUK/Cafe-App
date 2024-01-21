package cafe.app.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cafe.app.adapters.NotificationsAdapter
import cafe.app.database.DBHelper
import cafe.app.databinding.FragmentNotificationsBinding
import com.google.firebase.auth.FirebaseAuth

class NotificationsFragment : Fragment() {

    private lateinit var binding: FragmentNotificationsBinding
    private lateinit var viewModel: NotificationsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val factory = NotificationsViewModelFactory(requireActivity().applicationContext)
        viewModel = ViewModelProvider(this, factory)[NotificationsViewModel::class.java]

        setupRecyclerView()

        // Load orders for the logged-in customer
        viewModel.fetchOrders()

        return binding.root
    }

    private fun setupRecyclerView() {
        val dbHelper = DBHelper(requireContext())
        val adapter = NotificationsAdapter(dbHelper)
        binding.notificationsRecyclerView.adapter = adapter
        binding.notificationsRecyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.notifications.observe(viewLifecycleOwner) { notifications ->
            adapter.submitList(notifications)
        }
    }

    private fun getCurrentCustomerId(): Int {
        val dbHelper = DBHelper(requireContext())
        return FirebaseAuth.getInstance().currentUser?.uid?.let { firebaseUid ->
            dbHelper.getCustomerIdByFirebaseUid(firebaseUid) ?: -1
        } ?: -1
    }
}

