package cafe.app.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import cafe.app.appclasses.Customer
import cafe.app.appclasses.Order
import cafe.app.appclasses.OrderDetail
import cafe.app.appclasses.Payment
import cafe.app.appclasses.Product
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/* Database Config*/
private val DataBaseName = "CourseWorkDB.db"
private val ver : Int = 4

class DBHelper(context: Context) : SQLiteOpenHelper(context, DataBaseName,null , ver) {

    /* Customer Table */
    private val CustomerTableName = "TCustomer"
    private val Customer_Column_ID = "CusId"
    private val Customer_Column_FullName = "CusFullName"
    private val Customer_Column_Email = "CusEmail"
    private val Customer_Column_PhoneNo = "CusPhoneNo"
    private val Customer_Column_UserName = "CusUserName"
    private val Customer_Column_Password = "CusPassword"
    private val Customer_Column_IsActive = "CusIsActive"
    private val Customer_Column_FirebaseUID = "CusFirebaseUID"

    /* Admin Table */
    private val AdminTableName = "TAdmin"
    private val Admin_Column_ID = "AdminId"
    private val Admin_Column_FullName = "AdminFullName"
    private val Admin_Column_Email = "AdminEmail"
    private val Admin_Column_PhoneNo = "AdminPhoneNo"
    private val Admin_Column_UserName = "AdminUserName"
    private val Admin_Column_Password = "AdminPassword"
    private val Admin_Column_IsActive = "AdminIsActive"

    // Define other tables here

    /* Product Table */
    private val ProductTableName = "TProduct"
    private val Product_Column_ID = "ProductID"
    private val Product_Column_Name = "ProductName"
    private val Product_Column_Price = "ProductPrice"
    private val Product_Column_Image = "ProductImage"
    private val Product_Column_Available = "ProductAvailable"
    private val Product_Column_Category = "ProductCategory"

    /* Order Table */
    private val OrderTableName = "TOrder"
    private val Order_Column_ID = "OrderID"
    private val Order_Column_CustomerID = "CustomerID"
    private val Order_Column_Date = "OrderDate"
    private val Order_Column_Time = "OrderTime"
    private val Order_Column_Status = "OrderStatus"

    /* Order Details Table */
    private val OrderDetailsTableName = "TOrderDetails"
    private val OrderDetails_Column_ID = "OrderDetailsID"
    private val OrderDetails_Column_OrderID = "OrderID"
    private val OrderDetails_Column_ProductID = "ProductID"
    // Assuming the addition of Quantity and TotalPrice columns as discussed
    private val OrderDetails_Column_Quantity = "Quantity"
    private val OrderDetails_Column_TotalPrice = "TotalPrice"

    /* Payment Table */
    private val PaymentTableName = "TPayment"
    private val Payment_Column_ID = "PaymentID"
    private val Payment_Column_OrderID = "OrderID"
    private val Payment_Column_Type = "PaymentType"
    private val Payment_Column_Amount = "Amount"
    private val Payment_Column_Date = "PaymentDate"

    /* Feedback Table */
    private val FeedbackTableName = "TFeedback"
    private val Feedback_Column_OrderID = "OrderID"
    private val Feedback_Column_Score = "FeedbackScore"
    private val Feedback_Column_Comments = "FeedbackComments"

    // ..............................................................................
    // This is called the first time a database is accessed
    // Create a new database if not exist
    override fun onCreate(db: SQLiteDatabase?) {

        // Create Customer table
        var sqlCreateStatement: String = "CREATE TABLE $CustomerTableName ( $Customer_Column_ID INTEGER PRIMARY KEY AUTOINCREMENT, $Customer_Column_FullName TEXT NOT NULL, " +
                                         " $Customer_Column_Email TEXT NOT NULL, $Customer_Column_PhoneNo TEXT NOT NULL, $Customer_Column_UserName TEXT NOT NULL, " +
                                         " $Customer_Column_Password TEXT NOT NULL, $Customer_Column_IsActive INTEGER NOT NULL, $Customer_Column_FirebaseUID TEXT NOT NULL )"

        db?.execSQL(sqlCreateStatement)
//..........................................................
        //Create Admin table
        sqlCreateStatement = "CREATE TABLE $AdminTableName ( $Admin_Column_ID INTEGER PRIMARY KEY AUTOINCREMENT, $Admin_Column_FullName TEXT NOT NULL, " +
                " $Admin_Column_Email TEXT NOT NULL, $Admin_Column_PhoneNo TEXT NOT NULL, $Admin_Column_UserName TEXT NOT NULL, " +
                " $Admin_Column_Password TEXT NOT NULL, $Admin_Column_IsActive INTEGER NOT NULL )"

        db?.execSQL(sqlCreateStatement)
//..........................................................
//     Create other tables here

        // Create Product table
        val sqlCreateProductTable = "CREATE TABLE $ProductTableName (" +
                "$Product_Column_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$Product_Column_Name TEXT NOT NULL, " +
                "$Product_Column_Price REAL NOT NULL, " +
                "$Product_Column_Image TEXT, " +
                "$Product_Column_Available INTEGER NOT NULL, " +
                "$Product_Column_Category TEXT NOT NULL)"
        db?.execSQL(sqlCreateProductTable)

        // Create Order table
        val sqlCreateOrderTable = "CREATE TABLE $OrderTableName (" +
                "$Order_Column_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$Order_Column_CustomerID INTEGER NOT NULL, " +
                "$Order_Column_Date TEXT NOT NULL, " +
                "$Order_Column_Time TEXT NOT NULL, " +
                "$Order_Column_Status INTEGER NOT NULL, " +
                "FOREIGN KEY ($Order_Column_CustomerID) REFERENCES $CustomerTableName($Customer_Column_ID))"
        db?.execSQL(sqlCreateOrderTable)

        // Create Order Details table
        val sqlCreateOrderDetailsTable = "CREATE TABLE $OrderDetailsTableName (" +
                "$OrderDetails_Column_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$OrderDetails_Column_OrderID INTEGER NOT NULL, " +
                "$OrderDetails_Column_ProductID INTEGER NOT NULL, " +
                "$OrderDetails_Column_Quantity INTEGER NOT NULL, " +
                "$OrderDetails_Column_TotalPrice REAL NOT NULL, " +
                "FOREIGN KEY ($OrderDetails_Column_OrderID) REFERENCES $OrderTableName($Order_Column_ID), " +
                "FOREIGN KEY ($OrderDetails_Column_ProductID) REFERENCES $ProductTableName($Product_Column_ID))"
        db?.execSQL(sqlCreateOrderDetailsTable)

        // Create Payment table
        val sqlCreatePaymentTable = "CREATE TABLE $PaymentTableName (" +
                "$Payment_Column_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$Payment_Column_OrderID INTEGER NOT NULL, " +
                "$Payment_Column_Type TEXT NOT NULL, " +
                "$Payment_Column_Amount REAL NOT NULL, " +
                "$Payment_Column_Date TEXT NOT NULL, " +
                "FOREIGN KEY ($Payment_Column_OrderID) REFERENCES $OrderTableName($Order_Column_ID))"
        db?.execSQL(sqlCreatePaymentTable)

        // Create Feedback Table
        var sqlCreateFeedbackStatement: String = """
            CREATE TABLE $FeedbackTableName (
                ${Feedback_Column_OrderID} INTEGER PRIMARY KEY,
                ${Feedback_Column_Score} INTEGER NOT NULL,
                ${Feedback_Column_Comments} TEXT,
                FOREIGN KEY(${Feedback_Column_OrderID}) REFERENCES $OrderTableName($Order_Column_ID)
        )
        """.trimIndent()

        db?.execSQL(sqlCreateFeedbackStatement)
    }

    // This is called if the database ver. is changed
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS TCustomer")
        db?.execSQL("DROP TABLE IF EXISTS TAdmin")
        db?.execSQL("DROP TABLE IF EXISTS TProduct")
        db?.execSQL("DROP TABLE IF EXISTS TOrder")
        db?.execSQL("DROP TABLE IF EXISTS TOrderDetails")
        db?.execSQL("DROP TABLE IF EXISTS TPayment")
        onCreate(db)
    }

//..........................................................
    // other functions

    // CUSTOMER CRUD FUNCTIONS
    fun addCustomer(fullName: String, email: String, phoneNo: String, userName: String, password: String, isActive: Int, firebaseUid: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("CusFullName", fullName)
            put("CusEmail", email)
            put("CusPhoneNo", phoneNo)
            put("CusUserName", userName)
            put("CusPassword", password)
            put("CusIsActive", isActive)
            put("CusFirebaseUID", firebaseUid)
        }
        val success = db.insert("TCustomer", null, contentValues)
        db.close()
        return success
    }

    fun getCustomerById(customerId: Int): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM TCustomer WHERE CusId = $customerId", null)
    }

    fun updateCustomer(customerId: Int, fullName: String, email: String, phoneNo: String, userName: String, password: String, isActive: Int): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("CusFullName", fullName)
            put("CusEmail", email)
            put("CusPhoneNo", phoneNo)
            put("CusUserName", userName)
            put("CusPassword", password)
            put("CusIsActive", isActive)
        }
        val success = db.update("TCustomer", contentValues, "CusId = ?", arrayOf(customerId.toString()))
        db.close()
        return success
    }

    fun deleteCustomer(customerId: Int): Int {
        val db = this.writableDatabase
        val success = db.delete("TCustomer", "CusId = ?", arrayOf(customerId.toString()))
        db.close()
        return success
    }

    // ADMIN CRUD OPERATIONS
    fun addAdmin(fullName: String, email: String, phoneNo: String, userName: String, password: String, isActive: Int): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("AdminFullName", fullName)
            put("AdminEmail", email)
            put("AdminPhoneNo", phoneNo)
            put("AdminUserName", userName)
            put("AdminPassword", password)
            put("AdminIsActive", isActive)
        }
        val success = db.insert("TAdmin", null, contentValues)
        db.close()
        return success
    }

    fun getAdminById(adminId: Int): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM TAdmin WHERE AdminId = $adminId", null)
    }

    fun updateAdmin(adminId: Int, fullName: String, email: String, phoneNo: String, userName: String, password: String, isActive: Int): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("AdminFullName", fullName)
            put("AdminEmail", email)
            put("AdminPhoneNo", phoneNo)
            put("AdminUserName", userName)
            put("AdminPassword", password)
            put("AdminIsActive", isActive)
        }
        val success = db.update("TAdmin", contentValues, "AdminId = ?", arrayOf(adminId.toString()))
        db.close()
        return success
    }

    fun deleteAdmin(adminId: Int): Int {
        val db = this.writableDatabase
        val success = db.delete("TAdmin", "AdminId = ?", arrayOf(adminId.toString()))
        db.close()
        return success
    }

    // PRODUCT CRUD FUNCTIONS
    fun addProduct(productName: String, productPrice: Double, productImage: String?, productAvailable: Int): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("ProductName", productName)
            put("ProductPrice", productPrice)
            put("ProductImage", productImage)
            put("ProductAvailable", productAvailable)
        }
        val success = db.insert("TProduct", null, contentValues)
        db.close()
        return success
    }

    fun getProductById(productId: Int): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM TProduct WHERE ProductID = $productId", null)
    }

    fun updateProduct(productId: Int, productName: String, productPrice: Double, productImage: String?, productAvailable: Int): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("ProductName", productName)
            put("ProductPrice", productPrice)
            put("ProductImage", productImage)
            put("ProductAvailable", productAvailable)
        }
        val success = db.update("TProduct", contentValues, "ProductID = ?", arrayOf(productId.toString()))
        db.close()
        return success
    }

    fun deleteProduct(productId: Int): Int {
        val db = this.writableDatabase
        val success = db.delete("TProduct", "ProductID = ?", arrayOf(productId.toString()))
        db.close()
        return success
    }

    // ORDER CRUD FUNCTIONS

    fun addOrder(customerId: Int, orderDate: String, orderTime: String, orderStatus: Int): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("CustomerID", customerId)
            put("OrderDate", orderDate)
            put("OrderTime", orderTime)
            put("OrderStatus", orderStatus)
        }
        val success = db.insert("TOrder", null, contentValues)
        db.close()
        return success
    }

    fun getOrderById(orderId: Int): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM TOrder WHERE OrderID = $orderId", null)
    }

    fun updateOrder(orderId: Int, customerId: Int, orderDate: String, orderTime: String, orderStatus: Int): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("CustomerID", customerId)
            put("OrderDate", orderDate)
            put("OrderTime", orderTime)
            put("OrderStatus", orderStatus)
        }
        val success = db.update("TOrder", contentValues, "OrderID = ?", arrayOf(orderId.toString()))
        db.close()
        return success
    }

    fun deleteOrder(orderId: Int): Int {
        val db = this.writableDatabase
        val success = db.delete("TOrder", "OrderID = ?", arrayOf(orderId.toString()))
        db.close()
        return success
    }

    // ORDER DETAILS CRUD FUNCTIONS
    fun addOrderDetail(orderId: Int, productId: Int, quantity: Int, totalPrice: Double): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("OrderID", orderId)
            put("ProductID", productId)
            put("Quantity", quantity)
            put("TotalPrice", totalPrice)
        }
        val success = db.insert("TOrderDetails", null, contentValues)
        db.close()
        return success
    }

    fun getOrderDetailById(orderDetailId: Int): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM TOrderDetails WHERE OrderDetailsID = $orderDetailId", null)
    }

    fun updateOrderDetail(orderDetailId: Int, orderId: Int, productId: Int, quantity: Int, totalPrice: Double): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("OrderID", orderId)
            put("ProductID", productId)
            put("Quantity", quantity)
            put("TotalPrice", totalPrice)
        }
        val success = db.update("TOrderDetails", contentValues, "OrderDetailsID = ?", arrayOf(orderDetailId.toString()))
        db.close()
        return success
    }

    fun deleteOrderDetail(orderDetailId: Int): Int {
        val db = this.writableDatabase
        val success = db.delete("TOrderDetails", "OrderDetailsID = ?", arrayOf(orderDetailId.toString()))
        db.close()
        return success
    }

    // PAYMENT CRUD FUNCTIONS

    fun addPayment(orderId: Int, paymentType: String, amount: Double, paymentDate: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("OrderID", orderId)
            put("PaymentType", paymentType)
            put("Amount", amount)
            put("PaymentDate", paymentDate)
        }
        val success = db.insert("TPayment", null, contentValues)
        db.close()
        return success
    }

    fun getPaymentById(paymentId: Int): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM TPayment WHERE PaymentID = $paymentId", null)
    }

    fun updatePayment(paymentId: Int, orderId: Int, paymentType: String, amount: Double, paymentDate: String): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("OrderID", orderId)
            put("PaymentType", paymentType)
            put("Amount", amount)
            put("PaymentDate", paymentDate)
        }
        val success = db.update("TPayment", contentValues, "PaymentID = ?", arrayOf(paymentId.toString()))
        db.close()
        return success
    }

    fun deletePayment(paymentId: Int): Int {
        val db = this.writableDatabase
        val success = db.delete("TPayment", "PaymentID = ?", arrayOf(paymentId.toString()))
        db.close()
        return success
    }

    // OTHER FUNCTIONS

    // ADMIN
    fun importAdminFromCSV (context: Context) {
        val db = this.writableDatabase
        var bufferedReader: BufferedReader? = null

        try {
            val inputStream = context.assets.open("admins2.csv")
            bufferedReader = BufferedReader(InputStreamReader(inputStream))
            bufferedReader.readLine() // Skip the header line

            var line: String? = bufferedReader.readLine()
            while (line != null) {
                val tokens = line.split(",")
                if (tokens.size >= 5) {
                    val cursor = db.query(
                        "TAdmin",
                        arrayOf("AdminFullName"),
                        "AdminFullName = ?",
                        arrayOf(tokens[0]),
                        null,
                        null,
                        null
                    )

                    if (!cursor.moveToFirst()) {
                        val contentValues = ContentValues().apply {
                            put("AdminFullName", tokens[0])
                            put("AdminEmail", tokens[1])
                            put("AdminPhoneNo", tokens[2])
                            put("AdminUserName", tokens[3])
                            put("AdminPassword", tokens[4])
                            put("AdminIsActive", tokens[5].toInt())
                        }
                        db.insert("TAdmin", null, contentValues)
                    }
                    cursor.close()
                }
                line = bufferedReader.readLine()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                bufferedReader?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            db.close()
        }
    }

    fun adminCheck(email: String): Boolean {

        // val admin = dbHelper.getAdminByEmail(email)
        val db = this.readableDatabase
        // val adminEmail = admin?.email
        val cursor : Cursor = db.query(
            "TAdmin",
            null,
            "${"AdminEmail"} = ?",
            arrayOf(email),
            null,
            null,
            null
        )
        return cursor.moveToFirst()
    }

    // CUSTOMERS
    fun fetchUserDetails(userId: String): Customer? {
        val db = this.readableDatabase
        val cursor = db.query(
            "TCustomer",
            null,
            "CusId = ?",
            arrayOf(userId),
            null,
            null,
            null
        )
        with(cursor) {
            if (moveToFirst()) {
                val customer = Customer(
                    id = getInt(getColumnIndexOrThrow("CusId")),
                    fullName = getString(getColumnIndexOrThrow("CusFullName")),
                    email = getString(getColumnIndexOrThrow("CusEmail")),
                    phoneNo = getString(getColumnIndexOrThrow("CusPhoneNo")),
                    userName = getString(getColumnIndexOrThrow("CusUserName")),
                    password = getString(getColumnIndexOrThrow("CusPassword")),
                    isActive = getInt(getColumnIndexOrThrow("CusIsActive")),
                    firebaseUid = getString(getColumnIndexOrThrow("CusFirebaseUID"))
                )
                close()
                return customer
            }
        }
        cursor.close()
        return null
    }

    fun fetchUserDetailsByFirebaseUid(firebaseUid: String): Customer? {
        val db = this.readableDatabase
        val cursor = db.query(
            "TCustomer",
            null, // all columns
            "CusFirebaseUID = ?", // selection
            arrayOf(firebaseUid), // selection args
            null, // groupBy
            null, // having
            null // orderBy
        )
        with(cursor) {
            if (moveToFirst()) {
                val customer = Customer(
                    id = getInt(getColumnIndexOrThrow("CusId")),
                    fullName = getString(getColumnIndexOrThrow("CusFullName")),
                    email = getString(getColumnIndexOrThrow("CusEmail")),
                    phoneNo = getString(getColumnIndexOrThrow("CusPhoneNo")),
                    userName = getString(getColumnIndexOrThrow("CusUserName")),
                    password = getString(getColumnIndexOrThrow("CusPassword")),
                    isActive = getInt(getColumnIndexOrThrow("CusIsActive")),
                    firebaseUid = getString(getColumnIndexOrThrow("CusFirebaseUID"))
                )
                close()
                return customer
            }
        }
        cursor.close()
        return null
    }

    fun updateCustomerSelective(firebaseUid: String, fullName: String?, email: String?, phoneNo: String?, userName: String?, password: String?, isActive: Int? = null) {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            fullName?.let { put("CusFullName", it) }
            email?.let { put("CusEmail", it) }
            phoneNo?.let { put("CusPhoneNo", it) }
            userName?.let { put("CusUserName", it) }
            password?.let { put("CusPassword", it) } // Consider security implications
            isActive?.let { put("CusIsActive", it) }
        }
        // Use Firebase UID for selection
        db.update("TCustomer", contentValues, "CusFirebaseUID = ?", arrayOf(firebaseUid))
        db.close()
    }

    fun getCustomerIdByFirebaseUid(firebaseUid: String): Int? {
        val db = this.readableDatabase
        val cursor = db.query(
            "TCustomer",
            arrayOf("CusId"),
            "CusFirebaseUID = ?",
            arrayOf(firebaseUid),
            null,
            null,
            null,
            "1"
        )
        val customerId: Int? = if (cursor.moveToFirst()) cursor.getInt(cursor.getColumnIndex("CusId")) else null
        cursor.close()
        return customerId
    }


    // PRODUCTS
    fun importProductsFromCSV(context: Context) {
        val db = this.writableDatabase
        var bufferedReader: BufferedReader? = null

        try {
            val inputStream = context.assets.open("products.csv")
            bufferedReader = BufferedReader(InputStreamReader(inputStream))
            bufferedReader.readLine() // Skip the header line

            var line: String? = bufferedReader.readLine()
            while (line != null) {
                val tokens = line.split(",")
                if (tokens.size >= 5) {
                    val cursor = db.query(
                        "TProduct",
                        arrayOf("ProductName"),
                        "ProductName = ?",
                        arrayOf(tokens[0]),
                        null,
                        null,
                        null
                    )

                    if (!cursor.moveToFirst()) {
                        val contentValues = ContentValues().apply {
                            put("ProductName", tokens[0])
                            put("ProductPrice", tokens[1].toDouble())
                            put("ProductImage", if (tokens[2].isNotEmpty()) tokens[2] else null)
                            put("ProductAvailable", tokens[3].toInt())
                            put("ProductCategory", tokens[4])
                        }
                        db.insert("TProduct", null, contentValues)
                    }
                    cursor.close()
                }
                line = bufferedReader.readLine()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                bufferedReader?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            db.close()
        }
    }

    fun getAllProductsByCategory(): Map<String, List<Product>> {
        val db = this.readableDatabase
        val productsByCategory = mutableMapOf<String, MutableList<Product>>()

        val cursor = db.query("TProduct", null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("ProductID"))
            val name = cursor.getString(cursor.getColumnIndexOrThrow("ProductName"))
            val price = cursor.getDouble(cursor.getColumnIndexOrThrow("ProductPrice"))
            val image = cursor.getString(cursor.getColumnIndexOrThrow("ProductImage"))
            val isAvailable = cursor.getInt(cursor.getColumnIndexOrThrow("ProductAvailable"))
            val category = cursor.getString(cursor.getColumnIndexOrThrow("ProductCategory"))

            val product = Product(id, name, price, image, isAvailable, category)

            if (productsByCategory[category] == null) {
                productsByCategory[category] = mutableListOf(product)
            } else {
                productsByCategory[category]?.add(product)
            }
        }
        cursor.close()
        db.close()

        return productsByCategory
    }

    fun getProductNameById(productId: Int): String {
        val db = this.readableDatabase
        val cursor = db.query(
            "TProduct", // Table name
            arrayOf("ProductName"), // Columns to return
            "ProductID = ?", // Selection criteria
            arrayOf(productId.toString()), // Selection arguments
            null, // Group by
            null, // Having
            null, // Order by
            "1" // Limit to 1 result
        )
        var productName = ""
        if (cursor.moveToFirst()) {
            productName = cursor.getString(cursor.getColumnIndexOrThrow("ProductName"))
        }
        cursor.close()
        return productName
    }


    // ORDERS

    fun getOrdersByCustomerId(customerId: Int): List<Order> {
        val orders = mutableListOf<Order>()
        val db = this.readableDatabase
        val cursor = db.query(
            "TOrder",
            null,
            "CustomerID = ?",
            arrayOf(customerId.toString()),
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val order = Order(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("OrderID")),
                    customerId = cursor.getInt(cursor.getColumnIndexOrThrow("CustomerID")),
                    date = cursor.getString(cursor.getColumnIndexOrThrow("OrderDate")),
                    time = cursor.getString(cursor.getColumnIndexOrThrow("OrderTime")),
                    status = cursor.getInt(cursor.getColumnIndexOrThrow("OrderStatus"))
                )
                orders.add(order)
            } while (cursor.moveToNext())
        }
        cursor.close()

        return orders
    }

    // ORDER DETAILS

    fun getOrderDetailsByOrderId(orderId: Int): List<OrderDetail> {
        val db = this.readableDatabase
        val cursor = db.query(
            "TOrderDetails",
            null,
            "OrderID = ?",
            arrayOf(orderId.toString()),
            null,
            null,
            null
        )
        val orderDetails = mutableListOf<OrderDetail>()
        while (cursor.moveToNext()) {
            val orderDetail = OrderDetail(
                id = cursor.getInt(cursor.getColumnIndex("OrderDetailsID")),
                orderId = cursor.getInt(cursor.getColumnIndex("OrderID")),
                productId = cursor.getInt(cursor.getColumnIndex("ProductID")),
                quantity = cursor.getInt(cursor.getColumnIndex("Quantity")),
                totalPrice = cursor.getDouble(cursor.getColumnIndex("TotalPrice"))
            )
            orderDetails.add(orderDetail)
        }
        cursor.close()
        return orderDetails
    }

    // PAYMENTS

    // Fetch all payments for a specific order
    fun getPaymentsForOrder(orderId: Int): List<Payment> {
        val payments = mutableListOf<Payment>()
        val db = this.readableDatabase
        val cursor = db.query(
            PaymentTableName,
            null,
            "$Payment_Column_OrderID = ?",
            arrayOf(orderId.toString()),
            null,
            null,
            null
        )

        while (cursor.moveToNext()) {
            val payment = Payment(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(Payment_Column_ID)),
                orderId = cursor.getInt(cursor.getColumnIndexOrThrow(Payment_Column_OrderID)),
                type = cursor.getString(cursor.getColumnIndexOrThrow(Payment_Column_Type)),
                amount = cursor.getDouble(cursor.getColumnIndexOrThrow(Payment_Column_Amount)),
                date = cursor.getString(cursor.getColumnIndexOrThrow(Payment_Column_Date))
            )
            payments.add(payment)
        }
        cursor.close()
        return payments
    }

    fun getPaymentByOrderId(orderId: Int): Payment? {
        val db = this.readableDatabase
        val cursor = db.query(
            "TPayment",
            null,
            "OrderID = ?",
            arrayOf(orderId.toString()),
            null,
            null,
            null
        )
        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("PaymentID"))
            val type = cursor.getString(cursor.getColumnIndexOrThrow("PaymentType"))
            val amount = cursor.getDouble(cursor.getColumnIndexOrThrow("Amount"))
            val date = cursor.getString(cursor.getColumnIndexOrThrow("PaymentDate"))
            cursor.close()
            return Payment(id, orderId, type, amount, date)
        }
        cursor.close()
        return null
    }

    // FEEDBACK

    fun addFeedback(orderId: Int, score: Int, comments: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(Feedback_Column_OrderID, orderId)
            put(Feedback_Column_Score, score)
            put(Feedback_Column_Comments, comments)
        }
        return db.insert(FeedbackTableName, null, contentValues).also { db.close() }
    }

    fun feedbackExists(orderId: Int): Boolean {
        val db = this.readableDatabase
        val cursor = db.query(FeedbackTableName, arrayOf(Feedback_Column_OrderID), "$Feedback_Column_OrderID = ?", arrayOf(orderId.toString()), null, null, null)
        val exists = cursor.moveToFirst()
        cursor.close()
        return exists
    }


}