package majed.eddin.marvelcharacters.utils

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import majed.eddin.marvelcharacters.R

class RoundedView : FrameLayout {
    private var topLeftCornerRadius: Float = 0f
    private var topRightCornerRadius: Float = 0f
    private var bottomLeftCornerRadius: Float = 0f
    private var bottomRightCornerRadius: Float = 0f

    constructor(context: Context) : super(context) {
        init(context, null, 0)

    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(context, attrs, defStyle)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyle: Int) {
        val typedArray: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.RoundedView, 0, 0)

        topLeftCornerRadius =
            typedArray.getDimension(R.styleable.RoundedView_topLeftCornerRadius, 0f)
        topRightCornerRadius =
            typedArray.getDimension(R.styleable.RoundedView_topRightCornerRadius, 0f)
        bottomLeftCornerRadius =
            typedArray.getDimension(R.styleable.RoundedView_bottomLeftCornerRadius, 0f)
        bottomRightCornerRadius =
            typedArray.getDimension(R.styleable.RoundedView_bottomRightCornerRadius, 0f)

        typedArray.recycle()

        setLayerType(View.LAYER_TYPE_SOFTWARE, null)

    }

    override fun dispatchDraw(canvas: Canvas?) {
        val count: Int = canvas!!.save()
        val path = Path()

        val cornerDimension: FloatArray = floatArrayOf(
            topLeftCornerRadius,
            topLeftCornerRadius,
            topRightCornerRadius,
            topRightCornerRadius,
            bottomRightCornerRadius,
            bottomRightCornerRadius,
            bottomLeftCornerRadius,
            bottomLeftCornerRadius
        )

        path.addRoundRect(
            RectF(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat()),
            cornerDimension,
            Path.Direction.CW
        )

        canvas.clipPath(path)

        super.dispatchDraw(canvas)
        canvas.restoreToCount(count)
    }

    fun setTopLeftCornerRadius(topLeftCornerRadius: Float) {
        this.topLeftCornerRadius = topLeftCornerRadius
        invalidate()
    }

    fun setTopRightCornerRadius(topRightCornerRadius: Float) {
        this.topRightCornerRadius = topRightCornerRadius
        invalidate()
    }

    fun setBottomLeftCornerRadius(bottomLeftCornerRadius: Float) {
        this.bottomLeftCornerRadius = bottomLeftCornerRadius
        invalidate()
    }

    fun setBottomRightCornerRadius(bottomRightCornerRadius: Float) {
        this.bottomRightCornerRadius = bottomRightCornerRadius
        invalidate()
    }
}