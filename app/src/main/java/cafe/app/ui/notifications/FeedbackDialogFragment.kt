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

class FeedbackDialogFragment : DialogFragment() {
    private var orderId: Int = -1

    interface FeedbackListener {
        fun onFeedbackSubmitted(orderId: Int, score: Int, comments: String)
    }

    var listener: FeedbackListener? = null

    companion object {
        const val ORDER_ID_KEY = "orderId"

        fun newInstance(orderId: Int, listener: FeedbackListener): FeedbackDialogFragment {
            val fragment = FeedbackDialogFragment().apply {
                this.listener = listener // Set listener
            }
            val args = Bundle().apply {
                putInt(ORDER_ID_KEY, orderId)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderId = arguments?.getInt(ORDER_ID_KEY, -1) ?: -1
    }

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

    private fun validateFeedback(score: Int, comments: String): Boolean {
        return score in 1..5 && comments.isNotBlank()
    }


    fun setFeedbackListener(listener: FeedbackListener) {
        this.listener = listener
    }
}
