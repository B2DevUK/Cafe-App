@file:Suppress("KDocUnresolvedReference")

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

/**
 * [MenuFragment]
 * Description: A fragment for displaying a menu of categories and products to the user.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Properties]
 * - [databaseHelper]: Helper class for database operations.
 * - [containerForCategories]: Container layout for displaying categories.
 * - [scrollView]: ScrollView for scrolling through categories.
 * - [cartViewModel]: ViewModel for managing cart items.
 *
 * [Methods]
 * - [onCreateView]: Inflates the fragment's layout and initializes views and properties.
 * - [onViewCreated]: Called when the fragment's view is created. Sets up category navigation and populates categories with products.
 * - [populateCategoriesWithProducts]: Populates the categories with products from the database.
 * - [setupCategoryNavigation]: Sets up category navigation buttons and click listeners.
 * - [scrollToCategory]: Scrolls the view to the selected category.
 */
class MenuFragment : Fragment() {

    private lateinit var databaseHelper: DBHelper
    private lateinit var containerForCategories: LinearLayout
    private lateinit var scrollView: ScrollView
    private lateinit var cartViewModel: CheckoutViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_menu, container, false)

        scrollView = view.findViewById(R.id.menuScroll)
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

    /**
     * [populateCategoriesWithProducts]
     * Description: Populates the categories with products from the database.
     */
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
                val productImageView = productBinding.productImage
                val addToCartButton = productBinding.addToCartButton

                productNameTextView.text = product.name
                productPriceTextView.text = getString(R.string.product_price, product.price)


                // Use Glide to load the product image from the URL
                Glide.with(this)
                    .load(product.image)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(productImageView)

                addToCartButton.setOnClickListener {
                    cartViewModel.addToCart(product)
                }

                binding.categoryContainer.addView(productBinding.root)
            }
            containerForCategories.addView(binding.root)
        }
    }

    /**
     * [setupCategoryNavigation]
     * Description: Sets up category navigation buttons and click listeners.
     */
    private fun setupCategoryNavigation() {
        view?.findViewById<TextView>(R.id.buttonTea)?.setOnClickListener { scrollToCategory("Tea") }
        view?.findViewById<TextView>(R.id.buttonCoffee)?.setOnClickListener { scrollToCategory("Coffee") }
        view?.findViewById<TextView>(R.id.buttonCakes)?.setOnClickListener { scrollToCategory("Cakes") }
        view?.findViewById<TextView>(R.id.buttonSnacks)?.setOnClickListener { scrollToCategory("Snacks") }
        view?.findViewById<TextView>(R.id.buttonMerch)?.setOnClickListener { scrollToCategory("Merchandise") }
    }

    /**
     * [scrollToCategory]
     * Description: Scrolls the view to the selected category.
     *
     * @param category: The name of the category to scroll to.
     */
    private fun scrollToCategory(category: String) {
        val categoryContainerView = containerForCategories.findViewWithTag<View>(category)
        categoryContainerView?.let {
            val scrollY = it.top
            scrollView.smoothScrollTo(0, scrollY)
        }
    }
}

