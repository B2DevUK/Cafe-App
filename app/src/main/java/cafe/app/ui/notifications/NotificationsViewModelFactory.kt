@file:Suppress("KDocUnresolvedReference")

package cafe.app.ui.notifications

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cafe.app.database.DBHelper

/**
 * [NotificationsViewModelFactory]
 * Description: Factory class for creating instances of [NotificationsViewModel].
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Properties]
 * - [context]: The Android application context used to create a DBHelper instance.
 *
 * [Constructor]
 * - [context]: The Android application context.
 *
 * [Methods]
 * - [create]: Creates and returns an instance of [NotificationsViewModel].
 *   - [modelClass]: The class of the ViewModel to be created.
 *   - [T]: The type of ViewModel.
 *
 * [Throws]
 * - [IllegalArgumentException]: If the provided [modelClass] is not assignable from [NotificationsViewModel].
 */
class NotificationsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    /**
     * [create]
     * Description: Creates and returns an instance of [NotificationsViewModel].
     *
     * @param modelClass The class of the ViewModel to be created.
     * @param T The type of ViewModel.
     *
     * @return An instance of [NotificationsViewModel].
     *
     * @throws [IllegalArgumentException] If the provided [modelClass] is not assignable from [NotificationsViewModel].
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            val dbHelper = DBHelper(context)
            return NotificationsViewModel(dbHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


