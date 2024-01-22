@file:Suppress("KDocUnresolvedReference")

package cafe.app.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cafe.app.adapters.NotificationsAdapter
import cafe.app.database.DBHelper
import cafe.app.databinding.FragmentNotificationsBinding
import com.google.firebase.auth.FirebaseAuth

/**
 * [NotificationsFragment]
 * Description: A fragment for displaying notifications, including order details and feedback submission.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Properties]
 * - [binding]: Binding object for the fragment's layout.
 * - [viewModel]: ViewModel for managing notifications.
 *
 * [Methods]
 * - [onCreateView]: Inflates the fragment's layout, initializes views and properties, and sets up the RecyclerView.
 * - [setupRecyclerView]: Initializes and configures the RecyclerView to display notifications.
 * - [getCurrentCustomerId]: Retrieves the current customer's ID based on their Firebase UID.
 */
class NotificationsFragment : Fragment() {

    private lateinit var binding: FragmentNotificationsBinding
    private lateinit var viewModel: NotificationsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val factory = NotificationsViewModelFactory(requireActivity().applicationContext)
        viewModel = ViewModelProvider(this, factory)[NotificationsViewModel::class.java]

        setupRecyclerView()

        viewModel.fetchOrders()

        return binding.root
    }

    /**
     * [setupRecyclerView]
     * Description: Initializes and configures the RecyclerView to display notifications.
     */
    private fun setupRecyclerView() {
        val dbHelper = DBHelper(requireContext())

        val feedbackListener = object : FeedbackDialogFragment.FeedbackListener {
            override fun onFeedbackSubmitted(orderId: Int, score: Int, comments: String) {
                dbHelper.addFeedback(orderId, score, comments)
                Toast.makeText(context, "Feedback submitted", Toast.LENGTH_SHORT).show()
            }
        }

        val adapter = NotificationsAdapter(dbHelper, feedbackListener)
        binding.notificationsRecyclerView.adapter = adapter
        binding.notificationsRecyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.notifications.observe(viewLifecycleOwner) { notifications ->
            adapter.submitList(notifications)
        }
    }

    /**
     * [getCurrentCustomerId]
     * Description: Retrieves the current customer's ID based on their Firebase UID.
     *
     * @return The customer's ID or -1 if not found.
     */
    private fun getCurrentCustomerId(): Int {
        val dbHelper = DBHelper(requireContext())
        return FirebaseAuth.getInstance().currentUser?.uid?.let { firebaseUid ->
            dbHelper.getCustomerIdByFirebaseUid(firebaseUid) ?: -1
        } ?: -1
    }
}


