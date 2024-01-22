@file:Suppress("KDocUnresolvedReference")

package cafe.app.ui.notifications

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cafe.app.appclasses.Order
import cafe.app.database.DBHelper
import com.google.firebase.auth.FirebaseAuth

/**
 * [NotificationsViewModel]
 * Description: ViewModel for managing notifications, including fetching orders for the logged-in customer.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Properties]
 * - [_notifications]: MutableLiveData for holding a list of orders as notifications.
 * - [notifications]: LiveData representing the list of orders as notifications.
 *
 * [Constructor]
 * - [dbHelper]: An instance of DBHelper used for database operations.
 *
 * [Methods]
 * - [fetchOrders]: Fetches orders for the logged-in customer and updates the [_notifications] LiveData.
 */
class NotificationsViewModel(private val dbHelper: DBHelper) : ViewModel() {

    private val _notifications = MutableLiveData<List<Order>>()
    val notifications: LiveData<List<Order>> = _notifications

    /**
     * [fetchOrders]
     * Description: Fetches orders for the logged-in customer and updates the [_notifications] LiveData.
     */
    fun fetchOrders() {
        val firebaseUid = FirebaseAuth.getInstance().currentUser?.uid
        Log.d("NotificationsVM", "Firebase UID: $firebaseUid")
        firebaseUid ?: return
        val customerId = dbHelper.getCustomerIdByFirebaseUid(firebaseUid)
        Log.d("NotificationsVM", "Customer ID: $customerId")
        customerId ?: return
        val orders = dbHelper.getOrdersByCustomerId(customerId)
        Log.d("NotificationsVM", "Fetched orders: ${orders.size}")
        _notifications.postValue(orders)
    }
}
