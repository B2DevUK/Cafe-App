package cafe.app.ui.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AdminViewModel: ViewModel() {

        // LiveData to hold the state or data relevant to the admin panel
        private val _adminData = MutableLiveData<String>()
        val adminData: LiveData<String> get() = _adminData

        // Function to update the admin data
        fun updateAdminData(newData: String) {
            _adminData.value = newData
        }
    }
