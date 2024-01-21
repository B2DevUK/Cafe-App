package cafe.app.ui.notifications

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cafe.app.appclasses.Order
import cafe.app.database.DBHelper
import com.google.firebase.auth.FirebaseAuth

class NotificationsViewModel(private val dbHelper: DBHelper) : ViewModel() {

    private val _notifications = MutableLiveData<List<Order>>()
    val notifications: LiveData<List<Order>> = _notifications

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