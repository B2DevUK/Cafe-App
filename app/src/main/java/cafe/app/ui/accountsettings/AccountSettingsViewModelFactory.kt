@file:Suppress("KDocUnresolvedReference")

package cafe.app.ui.accountsettings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cafe.app.database.DBHelper

/**
 * [AccountSettingsViewModelFactory]
 * Description: Factory class responsible for creating an instance of AccountSettingsViewModel.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Properties]
 * - [context]: The Android application context.
 *
 * [Methods]
 * - [create]: Creates an instance of the requested ViewModel class.
 */
class AccountSettingsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    /**
     * [create]
     * Description: Creates an instance of the requested ViewModel class.
     * - [modelClass]: The class of the ViewModel to be created.
     * Returns: An instance of the requested ViewModel class.
     * Throws: IllegalArgumentException if an unknown ViewModel class is requested.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountSettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AccountSettingsViewModel(DBHelper(context.applicationContext)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

