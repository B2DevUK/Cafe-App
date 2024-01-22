@file:Suppress("KDocUnresolvedReference")

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

/**
 * [NotificationsAdapter]
 * Description: Adapter for displaying notifications in a RecyclerView.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Properties]
 * - [dbHelper]: An instance of DBHelper for database operations.
 * - [feedbackListener]: A listener for handling feedback submission.
 *
 * [Constructor]
 * - [dbHelper]: An instance of DBHelper for database operations.
 * - [feedbackListener]: A listener for handling feedback submission.
 *
 * [Methods]
 * - [onCreateViewHolder]: Creates a new view holder for a notification item view.
 * - [onBindViewHolder]: Binds data to a notification item view.
 *
 * [Inner Class: NotificationViewHolder]
 * - Description: View holder for notification items.
 *
 * - [Properties]
 *   - [dbHelper]: An instance of DBHelper for database operations.
 *   - [feedbackListener]: A listener for handling feedback submission.
 *
 * - [Methods]
 *   - [bind]: Binds data to the notification item view.
 *
 * [Companion Object]
 * - [DiffCallback]: Defines how to compare items for efficient updates in the RecyclerView.
 */
class NotificationsAdapter(private val dbHelper: DBHelper, private val feedbackListener: FeedbackDialogFragment.FeedbackListener) : ListAdapter<Order, NotificationsAdapter.NotificationViewHolder>(DiffCallback) {

    /**
     * [onCreateViewHolder]
     * Description: Creates a new view holder for a notification item view.
     *
     * @param parent The parent view group.
     * @param viewType The view type (not used).
     *
     * @return A new [NotificationViewHolder] instance.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notification_container, parent, false)
        return NotificationViewHolder(view, dbHelper, feedbackListener)
    }

    /**
     * [onBindViewHolder]
     * Description: Binds data to a notification item view.
     *
     * @param holder The view holder for the notification item.
     * @param position The position of the item in the data set.
     */
    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val order = getItem(position)
        holder.bind(order)
    }

    /**
     * [NotificationViewHolder]
     * Description: View holder for notification items.
     *
     * [Properties]
     * - [dbHelper]: An instance of DBHelper for database operations.
     * - [feedbackListener]: A listener for handling feedback submission.
     *
     * [Methods]
     * - [bind]: Binds data to the notification item view.
     */
    class NotificationViewHolder(
        itemView: View,
        private val dbHelper: DBHelper,
        private val feedbackListener: FeedbackDialogFragment.FeedbackListener
    ) : RecyclerView.ViewHolder(itemView) {

        /**
         * [bind]
         * Description: Binds data to the notification item view.
         *
         * @param order The order to display in the notification.
         */
        fun bind(order: Order) {
            // Retrieve order details and payment information
            val orderDetails = dbHelper.getOrderDetailsByOrderId(order.id)
            val payment = dbHelper.getPaymentByOrderId(order.id)

            // Build a string with ordered items and their details
            val itemsStringBuilder = StringBuilder("Items Ordered:\n")
            orderDetails.forEach { detail ->
                val productName = dbHelper.getProductNameById(detail.productId)
                itemsStringBuilder.append("- $productName, Quantity: ${detail.quantity}, Price: £${detail.totalPrice}\n")
            }

            // Calculate total price and set order status text
            val totalPrice = orderDetails.sumOf { it.totalPrice }
            val statusText = if (order.status == 1) "Pending" else "Completed"
            val paymentMethod = payment?.type ?: "N/A"
            val paymentDate = payment?.date ?: "N/A"

            // Update UI elements with order details
            itemView.findViewById<TextView>(R.id.orderStatusTextView).text = "Order Status: $statusText"
            itemView.findViewById<TextView>(R.id.orderDetailsTextView).text = itemsStringBuilder.toString().trim()
            itemView.findViewById<TextView>(R.id.totalPriceTextView).text = "Total Price: £${totalPrice}"
            itemView.findViewById<TextView>(R.id.paymentMethodTextView).text = "Payment Method: $paymentMethod"
            itemView.findViewById<TextView>(R.id.orderIdTextView).text = "Order ID: ${order.id}"
            itemView.findViewById<TextView>(R.id.paymentDateTextView).text = "Payment Date: $paymentDate"

            // Show the "Leave Feedback" button if the order is completed
            if (order.status == 0) { // Complete Order Status
                val feedbackButton = itemView.findViewById<Button>(R.id.leaveFeedbackButton)
                feedbackButton.visibility = View.VISIBLE
                feedbackButton.setOnClickListener {
                    val feedbackDialog = FeedbackDialogFragment.newInstance(order.id, feedbackListener)
                    feedbackDialog.show((itemView.context as AppCompatActivity).supportFragmentManager, "FeedbackDialog")

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

