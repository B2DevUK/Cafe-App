package cafe.app.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cafe.app.R
import cafe.app.appclasses.Order
import cafe.app.database.DBHelper
import cafe.app.ui.notifications.FeedbackDialogFragment

class NotificationsAdapter(private val dbHelper: DBHelper, private val feedbackListener: FeedbackDialogFragment.FeedbackListener) : ListAdapter<Order, NotificationsAdapter.NotificationViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notification_container, parent, false)
        return NotificationViewHolder(view, dbHelper, feedbackListener)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val order = getItem(position)
        holder.bind(order)
    }

    class NotificationViewHolder(
        itemView: View,
        private val dbHelper: DBHelper,
        private val feedbackListener: FeedbackDialogFragment.FeedbackListener // Add this line
    ) : RecyclerView.ViewHolder(itemView) {
        fun bind(order: Order) {
            val orderDetails = dbHelper.getOrderDetailsByOrderId(order.id)
            val payment = dbHelper.getPaymentByOrderId(order.id)

            val itemsStringBuilder = StringBuilder("Items Ordered:\n")
            orderDetails.forEach { detail ->
                val productName = dbHelper.getProductNameById(detail.productId)
                itemsStringBuilder.append("- $productName, Quantity: ${detail.quantity}, Price: £${detail.totalPrice}\n")
            }
            val totalPrice = orderDetails.sumOf { it.totalPrice }
            val statusText = if (order.status == 1) "Pending" else "Completed"
            val paymentMethod = payment?.type ?: "N/A"
            val paymentDate = payment?.date ?: "N/A"

            itemView.findViewById<TextView>(R.id.orderStatusTextView).text = "Order Status: $statusText"
            itemView.findViewById<TextView>(R.id.orderDetailsTextView).text = itemsStringBuilder.toString().trim()
            itemView.findViewById<TextView>(R.id.totalPriceTextView).text = "Total Price: £${totalPrice}"
            itemView.findViewById<TextView>(R.id.paymentMethodTextView).text = "Payment Method: $paymentMethod"
            itemView.findViewById<TextView>(R.id.orderIdTextView).text = "Order ID: ${order.id}"
            itemView.findViewById<TextView>(R.id.paymentDateTextView).text = "Payment Date: $paymentDate"

            // Inside NotificationViewHolder, where you are showing the feedback dialog
            if (order.status == 0) { // Assuming 0 means completed
                val feedbackButton = itemView.findViewById<Button>(R.id.leaveFeedbackButton)
                feedbackButton.visibility = View.VISIBLE
                feedbackButton.setOnClickListener {
                    val feedbackDialog = FeedbackDialogFragment.newInstance(order.id, feedbackListener)
                    feedbackDialog.show((itemView.context as AppCompatActivity).supportFragmentManager, "FeedbackDialog")

                    // After showing the dialog, set the listener for feedback submission.
                    feedbackDialog.setFeedbackListener(object : FeedbackDialogFragment.FeedbackListener {
                        override fun onFeedbackSubmitted(orderId: Int, score: Int, comments: String) {
                            dbHelper.addFeedback(orderId, score, comments)
                        }
                    })
                }
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Order>() {
            override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean = oldItem == newItem
        }
    }
}
