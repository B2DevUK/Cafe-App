@file:Suppress("KDocUnresolvedReference")

package cafe.app.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import cafe.app.R

/**
 * [FeedbackDialogFragment]
 * Description: A DialogFragment for collecting feedback from the user.
 *
 * [Author]
 * Author Name: Brandon Sharp
 *
 * [Properties]
 * - [orderId]: The order ID for which feedback is being collected.
 *
 * [Companion Object]
 * - [ORDER_ID_KEY]: A constant key used for passing the order ID as an argument.
 * - [newInstance]: Factory method to create a new instance of [FeedbackDialogFragment].
 *
 * [Interfaces]
 * - [FeedbackListener]: An interface for handling feedback submission.
 *
 * [Constructor]
 * - None.
 *
 * [Methods]
 * - [onCreate]: Initializes the [orderId] property from the arguments.
 * - [onCreateView]: Creates the view for the dialog and sets up UI elements.
 * - [validateFeedback]: Validates the feedback data (score and comments).
 * - [setFeedbackListener]: Sets the [FeedbackListener] for handling feedback submission.
 */
class FeedbackDialogFragment : DialogFragment() {
    private var orderId: Int = -1

    interface FeedbackListener {
        fun onFeedbackSubmitted(orderId: Int, score: Int, comments: String)
    }

    var listener: FeedbackListener? = null

    companion object {
        const val ORDER_ID_KEY = "orderId"

        /**
         * [newInstance]
         * Description: Factory method to create a new instance of [FeedbackDialogFragment].
         *
         * @param orderId The order ID for which feedback is being collected.
         * @param listener The [FeedbackListener] for handling feedback submission.
         *
         * @return A new instance of [FeedbackDialogFragment].
         */
        fun newInstance(orderId: Int, listener: FeedbackListener): FeedbackDialogFragment {
            val fragment = FeedbackDialogFragment().apply {
                this.listener = listener
            }
            val args = Bundle().apply {
                putInt(ORDER_ID_KEY, orderId)
            }
            fragment.arguments = args
            return fragment
        }
    }

    /**
     * [onCreate]
     * Description: Initializes the [orderId] property from the arguments.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderId = arguments?.getInt(ORDER_ID_KEY, -1) ?: -1
    }

    /**
     * [onCreateView]
     * Description: Creates the view for the dialog and sets up UI elements.
     *
     * @param inflater The LayoutInflater object that can be used to inflate views.
     * @param container The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState A saved instance of the fragment (not used here).
     *
     * @return The inflated view for the dialog.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_feedback_dialog, container, false)
        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
        val commentsEditText = view.findViewById<EditText>(R.id.commentsEditText)
        val confirmButton = view.findViewById<Button>(R.id.feedbackConfirmButton)
        val cancelButton = view.findViewById<Button>(R.id.feedbackCancelButton)

        confirmButton.setOnClickListener {
            val score = ratingBar.rating.toInt()
            val comments = commentsEditText.text.toString()
            if (validateFeedback(score, comments)) {
                listener?.onFeedbackSubmitted(orderId, score, comments)
                dismiss()
            } else {
                Toast.makeText(context, "Please provide valid feedback", Toast.LENGTH_SHORT).show()
            }
        }

        cancelButton.setOnClickListener {
            dismiss()
        }

        return view
    }

    /**
     * [validateFeedback]
     * Description: Validates the feedback data (score and comments).
     *
     * @param score The user's rating score.
     * @param comments The user's feedback comments.
     *
     * @return `true` if the feedback is valid; `false` otherwise.
     */
    private fun validateFeedback(score: Int, comments: String): Boolean {
        return score in 1..5 && comments.isNotBlank()
    }

    /**
     * [setFeedbackListener]
     * Description: Sets the [FeedbackListener] for handling feedback submission.
     *
     * @param listener The [FeedbackListener] to be set.
     */
    fun setFeedbackListener(listener: FeedbackListener) {
        this.listener = listener
    }
}

