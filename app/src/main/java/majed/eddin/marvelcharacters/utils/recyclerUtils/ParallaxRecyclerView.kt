package majed.eddin.marvelcharacters.utils.recyclerUtils

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import majed.eddin.marvelcharacters.ui.base.ParallaxViewHolder

class ParallaxRecyclerView : RecyclerView {

    private var scrollListener: OnScrollListener? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init()
    }

    private fun init() {
        addOnScrollListener(defaultListener)
    }

    private val defaultListener: OnScrollListener = object : OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (scrollListener != null)
                scrollListener!!.onScrollStateChanged(recyclerView, newState)
        }


        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            for (i in 0 until recyclerView.childCount) {
                val viewHolder: ViewHolder =
                    recyclerView.getChildViewHolder(recyclerView.getChildAt(i))
                if (viewHolder is ParallaxViewHolder) {
                    viewHolder.animateImage()
                }
            }

            if (scrollListener != null)
                scrollListener!!.onScrolled(recyclerView, dx, dy)
        }
    }

    override fun addOnScrollListener(listener: OnScrollListener) {
        if (listener != defaultListener)
            this.scrollListener = listener
        else
            super.addOnScrollListener(listener)
    }

}