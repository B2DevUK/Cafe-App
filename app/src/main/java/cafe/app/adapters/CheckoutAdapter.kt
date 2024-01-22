@file:Suppress("KDocUnresolvedReference")

package cafe.app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cafe.app.appclasses.CartItem
import cafe.app.R
import cafe.app.databinding.CartItemLayoutBinding

/**
 * [CheckoutAdapter]
 * Description: An adapter class for managing and displaying cart items in a RecyclerView.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Properties]
 * - [context]: The application context.
 * - [incrementClickListener]: Listener for incrementing cart item quantity.
 * - [decrementClickListener]: Listener for decrementing cart item quantity.
 * - [removeClickListener]: Listener for removing cart items.
 *
 * [Methods]
 * - [onCreateViewHolder]: Inflates the cart item layout and initializes a view holder.
 * - [onBindViewHolder]: Binds cart item data to the view holder.
 * - [calculateTotalPrice]: Calculates the total price of all cart items.
 * - [CartItemViewHolder]: Inner view holder class for managing cart item views and interactions.
 * - [CartItemDiffCallback]: Callback for calculating item differences in the adapter.
 */
class CheckoutAdapter(
    private val context: Context,
    private val incrementClickListener: (CartItem) -> Unit,
    private val decrementClickListener: (CartItem) -> Unit,
    private val removeClickListener: (CartItem) -> Unit
) : ListAdapter<CartItem, CheckoutAdapter.CartItemViewHolder>(CartItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val binding =
            CartItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        val cartItem = getItem(position)
        holder.bind(cartItem)
    }

    /**
     * [calculateTotalPrice]
     * Description: Calculates the total price of all cart items.
     *
     * @return The total price of all cart items.
     */
    fun calculateTotalPrice(): Double {
        var totalPrice = 0.0
        currentList.forEach { cartItem ->
            totalPrice += cartItem.product.price * cartItem.quantity
        }
        return totalPrice
    }

    /**
     * [CartItemViewHolder]
     * Description: Inner view holder class for managing cart item views and interactions.
     *
     * @param binding: View binding for the cart item layout.
     */
    inner class CartItemViewHolder(private val binding: CartItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.incrementButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    incrementClickListener(getItem(position))
                }
            }

            binding.decrementButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    decrementClickListener(getItem(position))
                }
            }

            binding.removeButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    removeClickListener(getItem(position))
                }
            }
        }

        /**
         * [bind]
         * Description: Binds cart item data to the view holder.
         *
         * @param cartItem: The cart item to bind.
         */
        fun bind(cartItem: CartItem) {
            binding.productName.text = cartItem.product.name
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

    /**
     * [CartItemDiffCallback]
     * Description: Callback for calculating item differences in the adapter.
     */
    private class CartItemDiffCallback : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.product.id == newItem.product.id
        }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem == newItem
        }
    }
}

