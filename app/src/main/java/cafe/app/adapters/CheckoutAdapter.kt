package cafe.app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cafe.app.R
import cafe.app.appclasses.CartItem
import cafe.app.databinding.CartItemLayoutBinding

class CheckoutAdapter(private val context: Context) :
    ListAdapter<CartItem, CheckoutAdapter.CartItemViewHolder>(CartItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val binding =
            CartItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        val cartItem = getItem(position)
        holder.bind(cartItem)
    }

    inner class CartItemViewHolder(private val binding: CartItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cartItem: CartItem) {
            binding.productName.text = context.getString(R.string.product_name_placeholder)
            binding.productDescription.text = context.getString(R.string.product_description_placeholder)
            binding.productPrice.text = String.format(
                context.getString(R.string.price_placeholder),
                cartItem.product.price
            )
            binding.productQuantity.text = String.format(
                context.getString(R.string.quantity_placeholder),
                cartItem.quantity
            )

        }
    }

    private class CartItemDiffCallback : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.product.id == newItem.product.id
        }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem == newItem
        }
    }
}
