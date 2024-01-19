package cafe.App.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import cafe.App.appclasses.Notification

class NotificationsDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "NotificationsDatabase.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NOTIFICATIONS = "notifications"
        const val COLUMN_ID = "notifID"
        const val COLUMN_ORDER_ID = "orderID"
        const val COLUMN_CUSTOMER_ID = "customerID"
        const val COLUMN_ORDER_STATUS = "orderStatus"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Define your database schema and create the "notifications" table here
        val CREATE_NOTIFICATIONS_TABLE = ("CREATE TABLE IF NOT EXISTS " + TABLE_NOTIFICATIONS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_ORDER_ID + " INTEGER,"
                + COLUMN_CUSTOMER_ID + " INTEGER,"
                + COLUMN_ORDER_STATUS + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_ORDER_ID + ") REFERENCES orders(orderID),"
                + "FOREIGN KEY(" + COLUMN_CUSTOMER_ID + ") REFERENCES users(userID))")
        db.execSQL(CREATE_NOTIFICATIONS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Handle database schema upgrades if needed for the "notifications" table
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NOTIFICATIONS")
        onCreate(db)
    }

    // Add a new notification
    fun addNotification(notification: Notification): Long {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_ORDER_ID, notification.orderID)
        values.put(COLUMN_CUSTOMER_ID, notification.customerID)
        values.put(COLUMN_ORDER_STATUS, notification.orderStatus)
        val newRowId = db.insert(TABLE_NOTIFICATIONS, null, values)
        db.close()
        return newRowId
    }

    // Retrieve a notification by ID
    fun getNotificationById(notificationId: Long): Notification? {
        val db = readableDatabase
        val cursor: Cursor = db.query(
            TABLE_NOTIFICATIONS,
            null,
            "$COLUMN_ID = ?",
            arrayOf(notificationId.toString()),
            null,
            null,
            null
        )
        var notification: Notification? = null

        if (cursor.moveToFirst()) {
            val orderId = cursor.getLong(cursor.getColumnIndex(COLUMN_ORDER_ID))
            val customerId = cursor.getLong(cursor.getColumnIndex(COLUMN_CUSTOMER_ID))
            val orderStatus = cursor.getInt(cursor.getColumnIndex(COLUMN_ORDER_STATUS))
            notification = Notification(notificationId, orderId, customerId, orderStatus)
        }

        cursor.close()
        db.close()
        return notification
    }

    // Update a notification's information
    fun updateNotification(notificationId: Long, updatedNotification: Notification): Int {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_ORDER_ID, updatedNotification.orderID)
        values.put(COLUMN_CUSTOMER_ID, updatedNotification.customerID)
        values.put(COLUMN_ORDER_STATUS, updatedNotification.orderStatus)
        val rowsAffected = db.update(
            TABLE_NOTIFICATIONS,
            values,
            "$COLUMN_ID = ?",
            arrayOf(notificationId.toString())
        )
        db.close()
        return rowsAffected
    }

    // Delete a notification by ID
    fun deleteNotification(notificationId: Long): Int {
        val db = writableDatabase
        val rowsDeleted = db.delete(
            TABLE_NOTIFICATIONS,
            "$COLUMN_ID = ?",
            arrayOf(notificationId.toString())
        )
        db.close()
        return rowsDeleted
    }
}
