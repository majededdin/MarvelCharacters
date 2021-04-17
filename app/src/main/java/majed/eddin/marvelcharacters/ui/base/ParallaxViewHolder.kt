package majed.eddin.marvelcharacters.ui.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import majed.eddin.marvelcharacters.utils.imageUtils.ParallaxImageView

abstract class ParallaxViewHolder : RecyclerView.ViewHolder,
    ParallaxImageView.ParallaxImageListener {

    private var backgroundImage: ParallaxImageView

    abstract fun getParallaxImageId(): Int

    constructor(itemView: View) : super(itemView) {
        backgroundImage = itemView.findViewById(getParallaxImageId()) as ParallaxImageView
        backgroundImage.setListener(this)
    }


    override fun requireValuesForTranslate(): IntArray? {
        if (itemView.parent == null) {
            // Not added to parent yet!
            return null
        } else {
            val itemPosition = IntArray(2)
            itemView.getLocationOnScreen(itemPosition)

            val recyclerPosition = IntArray(2)
            (itemView.parent as RecyclerView).getLocationOnScreen(recyclerPosition)

            return intArrayOf(
                itemPosition[1],
                (itemView.parent as RecyclerView).measuredHeight,
                recyclerPosition[1]
            )
        }
    }

    fun animateImage() {
        getBackgroundImage().doTranslate()
    }

    private fun getBackgroundImage(): ParallaxImageView {
        return backgroundImage
    }
}