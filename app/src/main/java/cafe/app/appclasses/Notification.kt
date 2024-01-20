package cafe.app.appclasses
data class Notification(
    val notifID: Long,     // Unique identifier for the notification
    val orderID: Long,     // Identifier for the associated order
    val customerID: Long,  // Identifier for the associated customer
    val orderStatus: Int   // Status of the associated order
)

