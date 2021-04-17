package majed.eddin.marvelcharacters.utils.imageUtils

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import majed.eddin.marvelcharacters.R

class ParallaxImageView : AppCompatImageView {


    companion object {
        private const val DEFAULT_PARALLAX_RATIO: Float = 1.2f
    }

    private var parallaxRatio = DEFAULT_PARALLAX_RATIO

    private var DEFAULT_CENTER_CROP: Boolean = true
    private var shouldCenterCrop: Boolean = DEFAULT_CENTER_CROP

    private var needToTranslate: Boolean = true
    private var listener: ParallaxImageListener? = null

    private var rowYPos: Int = -1
    private var recyclerViewHeight: Int = -1
    private var recyclerViewYPos: Int = -1

    interface ParallaxImageListener {
        fun requireValuesForTranslate(): IntArray?
    }

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        scaleType = ScaleType.MATRIX

        if (attrs != null) {
            val ta: TypedArray = context.obtainStyledAttributes(
                attrs,
                R.styleable.ParallaxImageView,
                0,
                0
            )
            this.parallaxRatio = ta.getFloat(
                R.styleable.ParallaxImageView_parallax_ratio,
                DEFAULT_PARALLAX_RATIO
            )
            this.shouldCenterCrop = ta.getBoolean(
                R.styleable.ParallaxImageView_center_crop,
                DEFAULT_CENTER_CROP
            )
            ta.recycle()
        }
    }

    /**
     * This trick was needed because there is no way to detect when image is displayed,
     * we need to translate image for very first time as well. This will be needed only
     * if you are using async image loading...
     * <p/>
     * # If only there was another way to get notified when image has displayed.
     */
    // region EnsureTranslate

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        ensureTranslate()
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        ensureTranslate()
    }


    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        ensureTranslate()
    }


    override fun setImageBitmap(bm: Bitmap) {
        super.setImageBitmap(bm)
        ensureTranslate()
    }


    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        ensureTranslate()
    }


    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        ensureTranslate()
    }
    // endregion

    /**
     * Notify this view when it is back on recyclerView, so we can reset.
     */
    fun reuse() {
        this.needToTranslate = true
    }

    fun centerCrop(enable: Boolean) {
        this.shouldCenterCrop = enable
    }

    fun setParallaxRatio(parallaxRatio: Float) {
        this.parallaxRatio = parallaxRatio
    }

    fun setListener(listener: ParallaxImageListener) {
        this.listener = listener
    }

    private fun getListener(): ParallaxImageListener? {
        return listener
    }

    @Synchronized
    fun doTranslate(): Boolean {
        if (drawable == null) {
            return false
        }
        return if (getListener() != null && getValues()) {
            calculateAndMove()
            true
        } else {
            false
        }
    }


    private fun ensureTranslate(): Boolean {
        if (needToTranslate) {
            needToTranslate = !doTranslate()
        }
        return !needToTranslate
    }

    private fun getValues(): Boolean {
        val values: IntArray = getListener()?.requireValuesForTranslate() ?: return false

        this.rowYPos = values[0]
        this.recyclerViewHeight = values[1]
        this.recyclerViewYPos = values[2]
        return true
    }

    private fun calculateAndMove() {
        val distanceFromCenter: Float =
            ((recyclerViewYPos + recyclerViewHeight) / 2 - rowYPos).toFloat()

        var drawableHeight: Int = drawable.intrinsicHeight
        val imageViewHeight: Int = measuredHeight
        var scale = 1f
        if (shouldCenterCrop) {
            scale = recomputeImageMatrix()
            drawableHeight *= scale.toInt()
        }

        val difference: Float = (drawableHeight - imageViewHeight).toFloat()
        val move: Float = (distanceFromCenter / recyclerViewHeight) * difference * parallaxRatio

        moveTo((move / 2) - (difference / 2), scale)
    }

    private fun recomputeImageMatrix(): Float {
        val scale: Float
        val viewWidth: Int = width - paddingLeft - paddingRight
        val viewHeight: Int = height - paddingTop - paddingBottom
        val drawableWidth: Int = drawable.intrinsicWidth
        val drawableHeight: Int = drawable.intrinsicHeight

        scale = if (drawableWidth * viewHeight > drawableHeight * viewWidth) {
            viewHeight.toFloat() / drawableHeight.toFloat()
        } else {
            viewWidth.toFloat() / drawableWidth.toFloat()
        }

        return scale
    }

    private fun moveTo(move: Float, scale: Float) {
        val imageMatrix: Matrix = imageMatrix
        if (scale != 1f) {
            imageMatrix.setScale(scale, scale)
        }

        val matrixValues = FloatArray(9)
        imageMatrix.getValues(matrixValues)
        val current: Float = matrixValues[Matrix.MTRANS_Y]
        imageMatrix.postTranslate(0f, move - current)

        setImageMatrix(imageMatrix)
        invalidate()
    }
}