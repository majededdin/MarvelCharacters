package majed.eddin.marvelcharacters.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textview.MaterialTextView
import majed.eddin.marvelcharacters.R

/**
 * Constructor for the transient bottom bar.
 *
 * @param parent   The parent for this transient bottom bar.
 * @param content  The content view for this transient bottom bar.
 * @param callback The content view callback for this transient bottom bar.
 */
class BaseSnackBar(
    parent: ViewGroup,
    content: View,
    callback: ContentViewCallback
) : BaseTransientBottomBar<BaseSnackBar>(parent, content, callback) {

    fun setText(text: CharSequence): BaseSnackBar {
        val textView = getView().findViewById<MaterialTextView>(R.id.snackbar_text)
        textView.text = text
        return this
    }


    fun setAction(text: CharSequence, listener: View.OnClickListener): BaseSnackBar {
        val actionView: MaterialTextView = getView().findViewById(R.id.txt_action)
        actionView.text = text
        actionView.visibility = View.VISIBLE
        actionView.setOnClickListener { view: View? ->
            listener.onClick(view)
            dismiss()
        }
        return this
    }


    fun setDismiss(): BaseSnackBar {
        val button: MaterialTextView = getView().findViewById(R.id.txt_dismiss)
        button.text = context.getString(R.string.dismiss)
        button.visibility = View.VISIBLE
        button.setOnClickListener { dismiss() }
        return this
    }


    companion object {

        fun make(parent: ViewGroup): BaseSnackBar {
            val inflater = LayoutInflater.from(parent.context) as LayoutInflater
            val content: View = inflater.inflate(R.layout.item_snackbar_base, parent, false)
            content.background = parent.background

            val viewCallback = ContentViewCallback(content)
            val baseSnackBar = BaseSnackBar(parent, content, viewCallback)

            baseSnackBar.getView().setPadding(0, 0, 0, 0)
            baseSnackBar.duration = Snackbar.LENGTH_INDEFINITE

            return baseSnackBar
        }

        class ContentViewCallback(private val view: View) :
            com.google.android.material.snackbar.ContentViewCallback {

            override fun animateContentIn(delay: Int, duration: Int) {
                view.scaleY = 0f
                ViewCompat.animate(view).scaleY(1f).setDuration(duration.toLong()).startDelay =
                    delay.toLong()
            }

            override fun animateContentOut(delay: Int, duration: Int) {
                view.scaleY = 1f
                ViewCompat.animate(view).scaleY(0f).setDuration(duration.toLong()).startDelay =
                    delay.toLong()
            }
        }
    }

}