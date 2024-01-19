package cafe.App.database
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import cafe.App.appclasses.AppUser

class UsersDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "UsersDatabase.db"
        const val DATABASE_VERSION = 1
        const val TABLE_USERS = "users"
        const val COLUMN_ID = "userID"
        const val COLUMN_NAME = "name"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PASSWORD = "password"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Define your database schema and create the "users" table here
        val CREATE_USERS_TABLE = ("CREATE TABLE IF NOT EXISTS " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_PASSWORD + " TEXT)")
        db.execSQL(CREATE_USERS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Handle database schema upgrades if needed for the "users" table
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    // Add a new user
    fun addUser(appUser: AppUser): Long {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, appUser.name)
        values.put(COLUMN_EMAIL, appUser.email)
        values.put(COLUMN_PASSWORD, appUser.getPassword()) // Password retrieval through the function
        val newRowId = db.insert(TABLE_USERS, null, values)
        db.close()
        return newRowId
    }

    // Retrieve a user by ID
    fun getUserById(userId: Long): AppUser? {
        val db = readableDatabase
        val cursor: Cursor = db.query(
            TABLE_USERS,
            null,
            "$COLUMN_ID = ?",
            arrayOf(userId.toString()),
            null,
            null,
            null
        )
        var appUser: AppUser? = null

        if (cursor.moveToFirst()) {
            val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
            val email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL))
            val password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD))
            appUser = AppUser(email, password)
            appUser.name = name
        }

        cursor.close()
        db.close()
        return appUser
    }

    // Retrieve a user by email (for authentication)
    fun getUserByEmail(email: String): AppUser? {
        val db = readableDatabase
        val cursor: Cursor = db.query(
            TABLE_USERS,
            null,
            "$COLUMN_EMAIL = ?",
            arrayOf(email),
            null,
            null,
            null
        )
        var appUser: AppUser? = null

        if (cursor.moveToFirst()) {
            val userId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
            val password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD))
            appUser = AppUser(email, password)
            appUser.name = name
            appUser.userId = userId
        }

        cursor.close()
        db.close()
        return appUser
    }

    // Update a user's information
    fun updateUser(userId: Long, appUser: AppUser): Int {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, appUser.name)
        values.put(COLUMN_EMAIL, appUser.email)
        values.put(COLUMN_PASSWORD, appUser.getPassword()) // Password retrieval through the function
        val rowsAffected = db.update(
            TABLE_USERS,
            values,
            "$COLUMN_ID = ?",
            arrayOf(userId.toString())
        )
        db.close()
        return rowsAffected
    }

    // Delete a user by ID
    fun deleteUser(userId: Long): Int {
        val db = writableDatabase
        val rowsDeleted = db.delete(
            TABLE_USERS,
            "$COLUMN_ID = ?",
            arrayOf(userId.toString())
        )
        db.close()
        return rowsDeleted
    }
}
