package cafe.app.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import cafe.app.appclasses.Order

class OrdersDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "OrdersDatabase.db"
        const val DATABASE_VERSION = 1
        const val TABLE_ORDERS = "orders"
        const val COLUMN_ID = "orderID"
        const val COLUMN_CUSTOMER_ID = "customerID"
        const val COLUMN_PRODUCT_ID = "productID"
        const val COLUMN_ORDER_PRICE = "orderPrice"
        const val COLUMN_ORDER_STATUS = "orderStatus"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Define your database schema and create the "orders" table here
        val CREATE_ORDERS_TABLE = ("CREATE TABLE IF NOT EXISTS " + TABLE_ORDERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CUSTOMER_ID + " INTEGER,"
                + COLUMN_PRODUCT_ID + " INTEGER,"
                + COLUMN_ORDER_PRICE + " REAL,"
                + COLUMN_ORDER_STATUS + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_CUSTOMER_ID + ") REFERENCES users(userID))")
        db.execSQL(CREATE_ORDERS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Handle database schema upgrades if needed for the "orders" table
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ORDERS")
        onCreate(db)
    }

    // Add a new order
    fun addOrder(order: Order): Long {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_CUSTOMER_ID, order.customerId)
        values.put(COLUMN_PRODUCT_ID, order.productId)
        values.put(COLUMN_ORDER_PRICE, order.orderPrice)
        values.put(COLUMN_ORDER_STATUS, order.orderStatus)
        val newRowId = db.insert(TABLE_ORDERS, null, values)
        db.close()
        return newRowId
    }

    // Retrieve an order by ID
    fun getOrderById(orderId: Long): Order? {
        val db = readableDatabase
        val cursor: Cursor = db.query(
            TABLE_ORDERS,
            null,
            "$COLUMN_ID = ?",
            arrayOf(orderId.toString()),
            null,
            null,
            null
        )
        var order: Order? = null

        if (cursor.moveToFirst()) {
            val customerId = cursor.getLong(cursor.getColumnIndex(COLUMN_CUSTOMER_ID))
            val productId = cursor.getLong(cursor.getColumnIndex(COLUMN_PRODUCT_ID))
            val orderPrice = cursor.getDouble(cursor.getColumnIndex(COLUMN_ORDER_PRICE))
            val orderStatus = cursor.getInt(cursor.getColumnIndex(COLUMN_ORDER_STATUS))
            order = Order(orderId, customerId, productId, orderPrice, orderStatus)
        }

        cursor.close()
        db.close()
        return order
    }

    // Update an order's information
    fun updateOrder(orderId: Long, updatedOrder: Order): Int {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_CUSTOMER_ID, updatedOrder.customerId)
        values.put(COLUMN_PRODUCT_ID, updatedOrder.productId)
        values.put(COLUMN_ORDER_PRICE, updatedOrder.orderPrice)
        values.put(COLUMN_ORDER_STATUS, updatedOrder.orderStatus)
        val rowsAffected = db.update(
            TABLE_ORDERS,
            values,
            "$COLUMN_ID = ?",
            arrayOf(orderId.toString())
        )
        db.close()
        return rowsAffected
    }

    // Delete an order by ID
    fun deleteOrder(orderId: Long): Int {
        val db = writableDatabase
        val rowsDeleted = db.delete(
            TABLE_ORDERS,
            "$COLUMN_ID = ?",
            arrayOf(orderId.toString())
        )
        db.close()
        return rowsDeleted
    }
}