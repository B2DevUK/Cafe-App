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
import cafe.app.appclasses.CartItem
import cafe.app.database.ProductDatabaseHelper
import cafe.app.databinding.ProductContainerBinding
import cafe.app.databinding.CategoryContainerBinding
import cafe.app.ui.checkout.CheckoutViewModel

class MenuFragment : Fragment() {

    private lateinit var productsDatabaseHelper: ProductDatabaseHelper
    private lateinit var containerForCategories: LinearLayout
    private lateinit var scrollView: ScrollView
    private lateinit var cartViewModel: CheckoutViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_menu, container, false)

        scrollView = view.findViewById(R.id.scrollView)
        containerForCategories = view.findViewById(R.id.containerForCategories)
        cartViewModel = ViewModelProvider(requireActivity()).get(CheckoutViewModel::class.java)
        productsDatabaseHelper = ProductDatabaseHelper(requireContext())

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCategoryNavigation()
        populateCategoriesWithProducts()
    }

    private fun populateCategoriesWithProducts() {
        val productsByCategory = productsDatabaseHelper.getAllProductsByCategory()

        // Remove existing category containers (placeholders)
        containerForCategories.removeAllViews()

        for ((category, productList) in productsByCategory) {
            // Inflate the category container layout using data binding
            val binding = CategoryContainerBinding.inflate(layoutInflater)
            binding.root.tag = category

            val categoryTitleTextView = binding.categoryTitle
            categoryTitleTextView.text = category

            for (product in productList) {
                // Inflate the product container layout using data binding
                val productBinding = ProductContainerBinding.inflate(layoutInflater)
                val productNameTextView = productBinding.productName
                val productDescriptionTextView = productBinding.productDescription
                val productPriceTextView = productBinding.productPrice
                val addToCartButton = productBinding.addToCartButton

                productNameTextView.text = product.name
                productDescriptionTextView.text = product.description
                productPriceTextView.text = product.price.toString()

                // Handle adding product to cart when addToCartButton is clicked
                addToCartButton.setOnClickListener {
                    val cartItem = CartItem(product, 1, 1) // You can initially set the quantity to 1
                    cartViewModel.addToCart(cartItem)
                }

                // Add the product view to the category container
                binding.categoryContainer.addView(productBinding.root)
            }

            // Add the category container view to the main container
            containerForCategories.addView(binding.root)
        }
    }


    private fun setupCategoryNavigation() {
        view?.findViewById<TextView>(R.id.buttonTea)?.setOnClickListener { scrollToCategory("Tea") }
        view?.findViewById<TextView>(R.id.buttonCoffee)?.setOnClickListener { scrollToCategory("Coffee") }
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
