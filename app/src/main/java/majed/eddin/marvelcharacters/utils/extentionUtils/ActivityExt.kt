package majed.eddin.marvelcharacters.utils.extentionUtils

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import majed.eddin.marvelcharacters.R
import majed.eddin.marvelcharacters.data.consts.AppConst

// ------------------------------------------- Activity Slide InOut --------------------------------

fun Activity.intentWithSlideInOut(aClass: Class<*>) = intentWithSlideInOut(aClass, false)

fun Activity.intentWithSlideInOut(aClass: Class<*>, finish: Boolean) {
    startActivity(Intent(this, aClass))
    if (finish) finish()
    if (AppConst.instance.deviceCurrentLanguage == "ar")
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
    else
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
}

fun Activity.intentWithSlideInOutFinishAll(aClass: Class<*>) {
    startActivity(Intent(this, aClass))
    finishAffinity()
    if (AppConst.instance.deviceCurrentLanguage == "ar")
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
    else
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
}

// ------------------------------------------- Activity Bundle Slide InOut -------------------------

fun Activity.intentWithSlideInOut(aClass: Class<*>, bundle: Bundle) =
    intentWithSlideInOut(aClass, bundle, false)

fun Activity.intentWithSlideInOut(aClass: Class<*>, bundle: Bundle, finish: Boolean) {
    startActivity(Intent(this, aClass).putExtras(bundle))
    if (finish) finish()
    if (AppConst.instance.deviceCurrentLanguage == "ar")
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
    else
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
}

// ------------------------------------------- Fragment Slide UpDown -------------------------------

fun FragmentActivity.loadWithSlideUpDown(
    fragment: Fragment, containerView: Int, withBackStack: Boolean
) {
    supportFragmentManager.commit {
        setCustomAnimations(
            R.anim.slide_in_up, R.anim.slide_in_down, R.anim.slide_out_down, R.anim.slide_out_up
        )
        replace(containerView, fragment, fragment.javaClass.name)
        if (withBackStack)
            addToBackStack(fragment.javaClass.name)
    }
}


@Suppress("DEPRECATION")
fun Activity.transparentStatusBar() {
    this.window.decorView.systemUiVisibility =
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    //make fully Android Transparent Status bar
    if (Build.VERSION.SDK_INT >= 21) {
        setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        this.window.statusBarColor = Color.TRANSPARENT
    }
}

private fun Activity.setWindowFlag(bits: Int, on: Boolean) {
    val win = this.window
    val winParams = win.attributes
    if (on) {
        winParams.flags = winParams.flags or bits
    } else {
        winParams.flags = winParams.flags and bits.inv()
    }
    win.attributes = winParams
}
