package cafe.app.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import cafe.app.R
import cafe.app.appclasses.Product
import cafe.app.database.DBHelper
import cafe.app.databinding.MenuEditContainerBinding
import com.bumptech.glide.Glide


class AdminMenuFragment : Fragment() {

    private lateinit var dbHelper: DBHelper
    private lateinit var scrollView: ScrollView
    private lateinit var addbutton: ImageButton
    private lateinit var menuContainer: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_menu, container, false)

        scrollView = view.findViewById(R.id.scrollView)
        addbutton = view.findViewById(R.id.addButton)
        menuContainer = view.findViewById(R.id.menuContainer)


        dbHelper = DBHelper(requireContext())


        populateMenu()

        return view
    }

    private fun populateMenu() {
        val productsByCategory = dbHelper.getAllProductsByCategory()
        menuContainer.removeAllViews()

        for ((category, products) in productsByCategory) {
            for (product in products) {
                val binding = MenuEditContainerBinding.inflate(layoutInflater)
                val productNameTextView = binding.editTitle
                val productPriceTextView = binding.editPrice
                val productImageView = binding.editImage
                val productCategoryTextView = binding.editCategory
                val productEditButton = binding.buttonEdit


                productNameTextView.text = product.name
                productPriceTextView.text = "£" + product.price
                productCategoryTextView.text = product.category

                Glide.with(this)
                    .load(product.image) // Assuming 'image' is the URL stored in ProductImage column
                    .placeholder(R.drawable.ic_launcher_foreground) // Optional: a placeholder image
                    .error(R.drawable.ic_launcher_foreground) // Optional: an error image
                    .into(productImageView)

                productEditButton.setOnClickListener{openEditScreen(product)}


                menuContainer.addView(binding.root)
            }
        }
    }

    private fun openEditScreen(product: Product) {
        val frag = EditProductFragment()

        val args = Bundle()
        args.putInt("productId", product.id)
        frag.arguments = args

        frag.show(childFragmentManager, "EditProductFragment")
    }


}