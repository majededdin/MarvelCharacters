package majed.eddin.marvelcharacters.utils.blurView

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.view.View

class BlurKit {

    companion object {
        private var instance: BlurKit? = null

        fun init(context: Context) {
            if (instance != null) {
                return
            }

            instance = BlurKit()
            instance!!.rs = RenderScript.create(context)
        }

        fun getInstance(): BlurKit {
            if (instance == null) {
                throw RuntimeException("BlurKit not initialized!")
            }

            return instance!!
        }
    }

    private lateinit var rs: RenderScript


    fun blur(src: Bitmap, radius: Int): Bitmap {
        val input: Allocation = Allocation.createFromBitmap(rs, src)
        val output: Allocation = Allocation.createTyped(rs, input.type)
        val script: ScriptIntrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))

        script.setRadius(radius.toFloat())
        script.setInput(input)
        script.forEach(output)

        output.copyTo(src)
        return src
    }

    fun blur(src: View, radius: Int): Bitmap {
        val bitmap: Bitmap = getBitmapForView(src, 1f)
        return blur(bitmap, radius)
    }

    fun fastBlur(src: View, radius: Int, downscaleFactor: Float): Bitmap {
        val bitmap: Bitmap = getBitmapForView(src, downscaleFactor)
        return blur(bitmap, radius)
    }

    private fun getBitmapForView(src: View, downscaleFactor: Float): Bitmap {
        val bitmap: Bitmap = Bitmap.createBitmap(
            (src.width * downscaleFactor).toInt(),
            (src.height * downscaleFactor).toInt(),
            Bitmap.Config.ARGB_4444
        )

        val canvas = Canvas(bitmap)
        val matrix = Matrix()
        matrix.preScale(downscaleFactor, downscaleFactor)
        canvas.setMatrix(matrix)
        src.draw(canvas)

        return bitmap
    }

}