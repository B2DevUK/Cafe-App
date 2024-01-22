package cafe.app.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.database.getIntOrNull
import androidx.fragment.app.DialogFragment
import cafe.app.R
import cafe.app.appclasses.Product
import cafe.app.database.DBHelper

class EditProductFragment: DialogFragment() {

    private lateinit var editProductName: EditText
    private lateinit var editProductPrice: EditText
    private lateinit var editProductImageURL: EditText
    private lateinit var editProductCategory: EditText
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    private lateinit var dbHelper: DBHelper
    private lateinit var product: Product


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit, container, false)

        val productId = arguments?.getInt("productId")

        editProductName = view.findViewById(R.id.editTextProductName)
        editProductPrice = view.findViewById(R.id.editPrice)
        editProductImageURL = view.findViewById(R.id.editImageURL)
        editProductCategory = view.findViewById(R.id.editCategory)
        saveButton = view.findViewById(R.id.buttonSave)
        cancelButton = view.findViewById(R.id.buttonCancel)

        dbHelper = DBHelper(requireContext())

        productId?.let {

            val productCursor = dbHelper.getProductById(it)

            if (productCursor?.moveToFirst() == true) {

                product = Product(
                    id = productCursor.getInt(productCursor.getColumnIndexOrThrow("ProductID")),
                    name = productCursor.getString(productCursor.getColumnIndexOrThrow("ProductName")),
                    price = productCursor.getDouble(productCursor.getColumnIndexOrThrow("ProductPrice")),
                    image = productCursor.getString(productCursor.getColumnIndexOrThrow("ProductImage")),
                    isAvailable = productCursor.getInt(productCursor.getColumnIndexOrThrow("ProductAvailable")),
                    category = productCursor.getString(productCursor.getColumnIndexOrThrow("ProductCategory"))
                )

                setValues(product)
                saveButton.setOnClickListener{saveData()}
                cancelButton.setOnClickListener{cancel()}
            }

            productCursor?.close()
        }

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun setValues(product: Product) {

        editProductName.setText(product.name)
        editProductPrice.setText(product.price.toString())
        editProductImageURL.setText(product.image)
        editProductCategory.setText(product.category)


    }

    private fun saveData() {
        product.name = editProductName.text.toString()
        product.price = editProductPrice.text.toString().toDoubleOrNull() ?: 0.0
        product.image = editProductImageURL.text.toString()
        product.category = editProductCategory.text.toString()

            dbHelper.updateProduct(
                productId = product.id,
                productName = product.name,
                productPrice = product.price,
                productImage = product.image,
                productCategory = product.category,
                productAvailable = product.isAvailable
            )
        dismiss()
        }
    private fun cancel() {
        dismiss()
    }

    }