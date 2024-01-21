package cafe.app.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import cafe.app.R
import cafe.app.appclasses.Admin

class AdminFragment: Fragment() {

    private lateinit var adminBar: LinearLayout
   // private lateinit var adminView: FragmentContainerView
    private lateinit var adminViewModel: AdminViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_admin_panel, container, false)

        adminBar = view.findViewById(R.id.adminBar)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adminViewModel = ViewModelProvider(this).get(AdminViewModel::class.java)
        adminViewModel.updateAdminData("New Data from AdminFragment")
    }

}