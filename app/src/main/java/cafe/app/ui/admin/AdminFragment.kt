package cafe.app.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModelProvider
import cafe.app.R
import cafe.app.database.DBHelper

class AdminFragment: Fragment() {

    private lateinit var adminBar: LinearLayout
    private lateinit var adminViewModel: AdminViewModel
    private lateinit var dbHelper: DBHelper
    private lateinit var fragView: FragmentContainerView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        val view = inflater.inflate(R.layout.fragment_admin_panel, container, false)

            adminBar = view.findViewById(R.id.adminBar)
            fragView = view.findViewById(R.id.fragmentContainerView)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adminViewModel = ViewModelProvider(this).get(AdminViewModel::class.java)
        adminViewModel.updateAdminData("New Data from AdminFragment")
        view.findViewById<TextView>(R.id.buttonMenu)?.setOnClickListener{replaceFragment(AdminMenuFragment())}
    }
        private fun replaceFragment(fragment: Fragment) {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()

            val previousFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
            if (previousFragment != null) {
                transaction.remove(previousFragment)
            }

            transaction.replace(R.id.fragmentContainerView, AdminMenuFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }
}


