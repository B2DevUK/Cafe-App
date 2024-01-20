package cafe.app.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import cafe.app.appclasses.Admin

class AdminDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "AdminDatabase.db"
        const val DATABASE_VERSION = 1
        const val TABLE_ADMIN = "admin"
        const val COLUMN_ID = "adminID"
        const val COLUMN_NAME = "name"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PASSWORD = "password"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Define your database schema and create the "admin" table here
        val CREATE_ADMIN_TABLE = ("CREATE TABLE IF NOT EXISTS " + TABLE_ADMIN + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_PASSWORD + " TEXT)")
        db.execSQL(CREATE_ADMIN_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Handle database schema upgrades if needed for the "admin" table
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ADMIN")
        onCreate(db)
    }

    // Add a new admin
    fun addAdmin(admin: Admin): Long {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, admin.name)
        values.put(COLUMN_EMAIL, admin.email)
        values.put(COLUMN_PASSWORD, admin.password)
        val newRowId = db.insert(TABLE_ADMIN, null, values)
        db.close()
        return newRowId
    }

    // Retrieve an admin by ID
    fun getAdminById(adminId: Long): Admin? {
        val db = readableDatabase
        val cursor: Cursor = db.query(
            TABLE_ADMIN,
            null,
            "$COLUMN_ID = ?",
            arrayOf(adminId.toString()),
            null,
            null,
            null
        )
        var admin: Admin? = null

        if (cursor.moveToFirst()) {
            val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
            val email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL))
            val password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD))
            admin = Admin(adminId, name, email, password)
        }

        cursor.close()
        db.close()
        return admin
    }

    // Update an admin's information
    fun updateAdmin(adminId: Long, updatedAdmin: Admin): Int {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, updatedAdmin.name)
        values.put(COLUMN_EMAIL, updatedAdmin.email)
        values.put(COLUMN_PASSWORD, updatedAdmin.password)
        val rowsAffected = db.update(
            TABLE_ADMIN,
            values,
            "$COLUMN_ID = ?",
            arrayOf(adminId.toString())
        )
        db.close()
        return rowsAffected
    }

    // Delete an admin by ID
    fun deleteAdmin(adminId: Long): Int {
        val db = writableDatabase
        val rowsDeleted = db.delete(
            TABLE_ADMIN,
            "$COLUMN_ID = ?",
            arrayOf(adminId.toString())
        )
        db.close()
        return rowsDeleted
    }
}
