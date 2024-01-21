package cafe.app.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cafe.app.R
import cafe.app.appclasses.Order
import cafe.app.database.DBHelper

class NotificationsAdapter(private val dbHelper: DBHelper) : ListAdapter<Order, NotificationsAdapter.NotificationViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notification_container, parent, false)
        return NotificationViewHolder(view, dbHelper)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val order = getItem(position)
        holder.bind(order)
    }

    class NotificationViewHolder(itemView: View, private val dbHelper: DBHelper) : RecyclerView.ViewHolder(itemView) {
        fun bind(order: Order) {
            val orderDetails = dbHelper.getOrderDetailsByOrderId(order.id)
            val itemsStringBuilder = StringBuilder("Items Ordered:\n")
            orderDetails.forEach { detail ->
                val productName = dbHelper.getProductNameById(detail.productId)
                itemsStringBuilder.append("- $productName, Quantity: ${detail.quantity}, Price: £${detail.totalPrice}\n")
            }
            val totalPrice = orderDetails.sumOf { it.totalPrice }

            itemView.findViewById<TextView>(R.id.orderDetailsTextView).text = itemsStringBuilder.toString().trim()
            itemView.findViewById<TextView>(R.id.totalPriceTextView).text = "Total Price: £${totalPrice}"
            itemView.findViewById<TextView>(R.id.paymentMethodTextView).text = "Payment method logic needs implementing."
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Order>() {
            override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean = oldItem == newItem
        }
    }
}
