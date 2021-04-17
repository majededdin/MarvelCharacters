package majed.eddin.marvelcharacters.ui.base

import android.os.Bundle
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import majed.eddin.marvelcharacters.R
import majed.eddin.marvelcharacters.data.model.modified.ErrorHandler
import majed.eddin.marvelcharacters.databinding.ActivityBaseBarBinding
import majed.eddin.marvelcharacters.ui.viewModel.BaseViewModel
import majed.eddin.marvelcharacters.utils.extentionUtils.loadWithSlideUpDown

abstract class BaseBarActivity<V : BaseViewModel> : BaseActivity<V>() {

    private lateinit var binding: ActivityBaseBarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBaseBarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        baseInit()
        initSwipeRefresh(binding.baseBarFrame)
    }


    fun getBaseFrame(): SwipeRefreshLayout = binding.baseBarFrame

    fun getSearchButton(): AppCompatImageButton = binding.btnSearch


    fun setOnSwipeRefreshStatus(status: Boolean) {
        binding.baseBarFrame.isEnabled = status
    }

    fun getBindingView(): ActivityBaseBarBinding = binding


    override fun setErrorHandler(handler: ErrorHandler) {
    }


    private fun baseInit() {
        setSupportActionBar(binding.baseToolbar)
        binding.baseToolbar.setNavigationOnClickListener { onBackPressed() }
    }


    fun loadSearch(fragment: Fragment) {
        loadWithSlideUpDown(
            fragment, R.id.container_search, true
        )
    }

}