package cafe.app.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cafe.app.R
import cafe.app.database.DBHelper
import cafe.app.databinding.ProductContainerBinding
import cafe.app.databinding.CategoryContainerBinding
import cafe.app.ui.checkout.CheckoutViewModel
import com.bumptech.glide.Glide

class MenuFragment : Fragment() {

    private lateinit var databaseHelper: DBHelper
    private lateinit var containerForCategories: LinearLayout
    private lateinit var scrollView: ScrollView
    private lateinit var cartViewModel: CheckoutViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_menu, container, false)

        scrollView = view.findViewById(R.id.scrollView)
        containerForCategories = view.findViewById(R.id.containerForCategories)
        cartViewModel = ViewModelProvider(requireActivity())[CheckoutViewModel::class.java]
        databaseHelper = DBHelper(requireContext())

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCategoryNavigation()
        populateCategoriesWithProducts()
    }

    private fun populateCategoriesWithProducts() {
        val productsByCategory = databaseHelper.getAllProductsByCategory()

        containerForCategories.removeAllViews()

        for ((category, productList) in productsByCategory) {
            val binding = CategoryContainerBinding.inflate(layoutInflater)
            binding.root.tag = category

            val categoryTitleTextView = binding.categoryTitle
            categoryTitleTextView.text = category

            for (product in productList) {
                val productBinding = ProductContainerBinding.inflate(layoutInflater)
                val productNameTextView = productBinding.productName
                val productPriceTextView = productBinding.productPrice
                val productImageView = productBinding.productImage // Assuming this is your ImageView
                val addToCartButton = productBinding.addToCartButton

                productNameTextView.text = product.name
                productPriceTextView.text = ("£" + product.price.toString())

                // Use Glide to load the product image from the URL
                Glide.with(this)
                    .load(product.image) // Assuming 'image' is the URL stored in ProductImage column
                    .placeholder(R.drawable.ic_launcher_foreground) // Optional: a placeholder image
                    .error(R.drawable.ic_launcher_foreground) // Optional: an error image
                    .into(productImageView)

                addToCartButton.setOnClickListener {
                    cartViewModel.addToCart(product)
                }

                binding.categoryContainer.addView(productBinding.root)
            }
            containerForCategories.addView(binding.root)
        }
    }

    private fun setupCategoryNavigation() {
        view?.findViewById<TextView>(R.id.buttonTea)?.setOnClickListener { scrollToCategory("Tea") }
        view?.findViewById<TextView>(R.id.buttonCoffee)?.setOnClickListener { scrollToCategory("Coffee") }
        view?.findViewById<TextView>(R.id.buttonCakes)?.setOnClickListener { scrollToCategory("Cakes") }
        view?.findViewById<TextView>(R.id.buttonSnacks)?.setOnClickListener { scrollToCategory("Snacks") }
        view?.findViewById<TextView>(R.id.buttonMerch)?.setOnClickListener { scrollToCategory("Merchandise") }
    }

    private fun scrollToCategory(category: String) {
        val categoryContainerView = containerForCategories.findViewWithTag<View>(category)
        categoryContainerView?.let {
            val scrollY = it.top
            scrollView.smoothScrollTo(0, scrollY)
        }
    }
}
