package cafe.app.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import cafe.app.appclasses.Product
import java.io.BufferedReader
import java.io.InputStreamReader

class ProductDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "CafeAppDatabase.db"
        const val DATABASE_VERSION = 1
        const val TABLE_PRODUCTS = "products"
        const val COLUMN_ID = "productID"
        const val COLUMN_NAME = "productName"
        const val COLUMN_DESCRIPTION = "productDesc"
        const val COLUMN_PRICE = "productPrice"
        const val COLUMN_IMAGE_URL = "productImg"
        const val COLUMN_CATEGORY = "productCategory"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Define your updated database schema and create the "products" table here
        val CREATE_PRODUCTS_TABLE = ("CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_PRICE + " REAL,"
                + COLUMN_IMAGE_URL + " TEXT,"
                + COLUMN_CATEGORY + " TEXT)")
        db.execSQL(CREATE_PRODUCTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Handle database schema upgrades if needed
        // You can modify tables or create new ones here
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTS")
        onCreate(db)
    }

    // Add a new product
    fun addProduct(product: Product) {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, product.name)
        values.put(COLUMN_DESCRIPTION, product.description)
        values.put(COLUMN_PRICE, product.price)
        values.put(COLUMN_IMAGE_URL, product.imageUrl)
        values.put(COLUMN_CATEGORY, product.category)
        db.insert(TABLE_PRODUCTS, null, values)
        db.close()
    }


    // Retrieve a product by ID
    @SuppressLint("Range")
    fun getProductById(id: Int): Product? {
        val db = readableDatabase
        val cursor: Cursor = db.query(
            TABLE_PRODUCTS,
            null,
            "$COLUMN_ID = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )
        var product: Product? = null

        if (cursor.moveToFirst()) {
            val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
            val description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION))
            val price = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE))
            val imageUrl = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_URL))
            val category = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY))
            product = Product(id, name, description, price, imageUrl, category) // Include category in the Product constructor
        }

        cursor.close()
        db.close()
        return product
    }


    // Update a product
    fun updateProduct(product: Product): Int {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, product.name)
        values.put(COLUMN_DESCRIPTION, product.description)
        values.put(COLUMN_PRICE, product.price)
        values.put(COLUMN_IMAGE_URL, product.imageUrl)
        values.put(COLUMN_CATEGORY, product.category)
        return db.update(
            TABLE_PRODUCTS,
            values,
            "$COLUMN_ID = ?",
            arrayOf(product.id.toString())
        )
    }

    // Delete a product by ID
    fun deleteProduct(id: Int): Int {
        val db = writableDatabase
        return db.delete(
            TABLE_PRODUCTS,
            "$COLUMN_ID = ?",
            arrayOf(id.toString())
        )
    }

    // Retrieve all products
    @SuppressLint("Range")
    fun getAllProducts(): List<Product> {
        val productList = mutableListOf<Product>()
        val db = readableDatabase
        val cursor: Cursor = db.query(
            TABLE_PRODUCTS,
            null,
            null,
            null,
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                val description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION))
                val price = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE))
                val imageUrl = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_URL))
                val category = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY))
                productList.add(Product(id, name, description, price, imageUrl, category))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return productList
    }


    // Retrieve products by category
    @SuppressLint("Range")
    fun getProductsByCategory(category: String): List<Product> {
        val productList = mutableListOf<Product>()
        val db = readableDatabase
        val cursor: Cursor = db.query(
            TABLE_PRODUCTS,
            null,
            "$COLUMN_CATEGORY = ?",
            arrayOf(category),
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                val description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION))
                val price = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE))
                val imageUrl = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_URL))
                val productCategory = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)) // Updated variable name
                productList.add(Product(id, name, description, price, imageUrl, productCategory)) // Include category in the Product constructor
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return productList
    }

    fun getAllProductsByCategory(): Map<String, List<Product>> {
        val productMap = mutableMapOf<String, MutableList<Product>>() // Change the value type to MutableList<Product>

        val allProducts = getAllProducts() // Assuming you have a function to retrieve all products

        for (product in allProducts) {
            val category = product.category

            if (!productMap.containsKey(category)) {
                productMap[category] = mutableListOf()
            }

            productMap[category]?.add(product) // Use ?. to safely call add on a nullable MutableList
        }

        // Convert all values to immutable lists
        return productMap.mapValues { entry -> entry.value.toList() }
    }

    fun importProductsFromCSV(context: Context) {
        val productDatabaseHelper = ProductDatabaseHelper(context)
        val inputStream = context.assets.open("products.csv")

        try {
            val reader = BufferedReader(InputStreamReader(inputStream))
            var line: String?

            // Read and process each line of the CSV file
            while (reader.readLine().also { line = it } != null) {
                val parts = line!!.split(",")

                if (parts.size == 6) {
                    // Parse the CSV data
                    val productId = parts[0].toInt()
                    val productName = parts[1]
                    val productPrice = parts[2].toDouble()
                    val productDesc = parts[3]
                    val productImg = parts[4]
                    val productCategory = parts[5]

                    // Check if the product with the same ID already exists in the database
                    val existingProduct = productDatabaseHelper.getProductById(productId)

                    if (existingProduct == null) {
                        // Create a new Product object
                        val product = Product(productId, productName, productDesc, productPrice, productImg, productCategory)

                        // Add the product to the database
                        productDatabaseHelper.addProduct(product)
                    } else {
                        continue // Skip this product and move to the next one
                    }
                }
            }

            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            inputStream.close()
            productDatabaseHelper.close()
        }
    }

}

