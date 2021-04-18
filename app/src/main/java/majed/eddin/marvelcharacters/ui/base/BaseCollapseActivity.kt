package majed.eddin.marvelcharacters.ui.base

import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.appbar.AppBarLayout
import com.squareup.picasso.Picasso
import majed.eddin.marvelcharacters.R
import majed.eddin.marvelcharacters.databinding.ActivityBaseCollapseBinding
import majed.eddin.marvelcharacters.ui.viewModel.BaseViewModel

abstract class BaseCollapseActivity<V : BaseViewModel> : BaseActivity<V>(),
    AppBarLayout.OnOffsetChangedListener {

    private lateinit var binding: ActivityBaseCollapseBinding

    // For AppBar Scrolling
    private var isShow = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBaseCollapseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        baseInit()
        initSwipeRefresh(binding.baseCollapseContainer)
    }


    private fun baseInit() {
        binding.baseToolbar.setNavigationOnClickListener { onBackPressed() }
        binding.baseAppBarCollapse.addOnOffsetChangedListener(this)
    }


    fun setOnSwipeRefreshStatus(status: Boolean) {
        binding.baseCollapseContainer.isEnabled = status
    }


    fun setCollapseMedia(path: String) {
        Picasso.get().load(path).into(binding.imgCharacter)
    }


    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        if (appBarLayout.totalScrollRange + verticalOffset == 0) {
            binding.baseCollapseToolbar.title = title
            isShow = true
            changeBarIconsColor(isShow)
        } else if (isShow) {
            binding.baseCollapseToolbar.title =
                " " //careFull there should a space between double quote otherwise it wont work
            isShow = false
            changeBarIconsColor(isShow)
        }
    }


    private fun changeBarIconsColor(isCollapsed: Boolean) {
        if (isCollapsed)
            changeStatusBarColor(R.color.colorBackToolBar)
        else
            changeStatusBarColor(R.color.colorBackToolBarLowOpacity)
    }


    fun getBaseFrame(): SwipeRefreshLayout = binding.baseCollapseContainer


    fun setBackgroundImage(path: String) {
        Picasso.get().load(path).into(binding.imgCharacterBase)
    }

}