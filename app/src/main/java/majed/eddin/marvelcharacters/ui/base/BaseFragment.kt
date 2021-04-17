package majed.eddin.marvelcharacters.ui.base

import am.networkconnectivity.NetworkConnectivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import majed.eddin.marvelcharacters.R
import majed.eddin.marvelcharacters.data.consts.AppConst
import majed.eddin.marvelcharacters.data.model.modified.ErrorHandler
import majed.eddin.marvelcharacters.data.remote.ApiResponse
import majed.eddin.marvelcharacters.data.remote.ApiStatus
import majed.eddin.marvelcharacters.ui.viewModel.BaseViewModel
import majed.eddin.marvelcharacters.utils.extentionUtils.toGone
import majed.eddin.marvelcharacters.utils.extentionUtils.toVisible

abstract class BaseFragment<V : BaseViewModel> : Fragment() {

    private var viewModel: V? = null
    private lateinit var layout: FrameLayout
    private lateinit var progressBar: ProgressBar
    lateinit var baseActivity: BaseActivity<*>
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout


    @LayoutRes
    protected abstract fun setLayout(): Int


    protected abstract fun getViewModel(): Class<V>?


    protected abstract fun viewModelInstance(viewModel: V?)


    abstract fun updateView()


    abstract fun setErrorHandler(handler: ErrorHandler)


    protected abstract fun viewInit()


    override fun onCreate(savedInstanceState: Bundle?) {
        viewModelInstance(initViewModel())
        super.onCreate(savedInstanceState)
    }


    private fun initViewModel(): V? {
//        BaseViewModelFactory factory = new BaseViewModelFactory(getApplication());
        if (getViewModel() != null)
            viewModel = ViewModelProvider(this).get(getViewModel()!!)
        return viewModel
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        layout = inflater.inflate(R.layout.fragment_base, container, false) as FrameLayout
        baseInit()
        initSwipeRefresh(swipeRefreshLayout)
        layoutInflater.inflate(setLayout(), swipeRefreshLayout, true)
        return layout
    }


    private fun baseInit() {
        baseActivity = activity as BaseActivity<*>
        swipeRefreshLayout = layout.findViewById(R.id.containerBaseFragment)
        progressBar = layout.findViewById(R.id.progress_bar)
        progressBar.toGone()
    }


    private fun initSwipeRefresh(swipeRefreshLayout: SwipeRefreshLayout) {
        swipeRefreshLayout.setColorSchemeColors(
            ContextCompat.getColor(
                baseActivity, R.color.colorPrimary
            ),
            ContextCompat.getColor(
                baseActivity,
                R.color.colorPrimaryVariant
            ),
            ContextCompat.getColor(
                baseActivity, R.color.colorSecondary
            )
        )

        swipeRefreshLayout.setOnRefreshListener { updateView() }
    }


    protected fun getSwipeRefresh() = swipeRefreshLayout


    protected fun setOnSwipeRefreshStatus(status: Boolean) {
        swipeRefreshLayout.isEnabled = status
    }


    open fun getCustomView() = layout


    open fun getDeviceCurrentLanguage(): String = AppConst.instance.deviceCurrentLanguage


    open fun showLog(message: String) = baseActivity.showLog(message)


    open fun showMessage(message: String) = baseActivity.showMessage(message)


    open fun showMessage(message: String, callback: BaseTransientBottomBar.BaseCallback<Snackbar>) =
        baseActivity.showMessage(message, callback)


    fun setLoadingStatus(status: Boolean) {
        showLoading(status)
    }


    private fun showLoading(status: Boolean) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                showProgressBar(status)

            } catch (ignored: IllegalStateException) {
            }
        }
    }


    private fun showProgressBar(status: Boolean) {
        if (status)
            progressBar.toVisible()
        else
            progressBar.toGone()
    }


    open fun handleApiResponse(apiResponse: ApiResponse<*>, failureListener: View.OnClickListener) {
        when (apiResponse.apiStatus) {

            ApiStatus.OnBackEndError -> {
                showProgressBar(false)
                baseActivity.failureMessage(apiResponse.message, failureListener)
            }

            ApiStatus.OnError -> {
                showProgressBar(false)
                if (apiResponse.errorBodyAsJSON != null)
                    baseActivity.getResponseErrors(apiResponse.errorBodyAsJSON!!)
                else
                    showMessage(apiResponse.message)
            }

            ApiStatus.OnFailure -> {
                showProgressBar(false)
                Log.e("OnFailure ", apiResponse.message)
            }

            ApiStatus.OnForbidden -> {
                showProgressBar(false)
                Log.e("OnForbidden ", apiResponse.message)
            }

            ApiStatus.OnHttpException -> {
                showProgressBar(false)
                showMessage(apiResponse.message)
            }

            ApiStatus.OnLoading -> showProgressBar(true)

            ApiStatus.OnNotFound -> {
                showProgressBar(false)
                showMessage(apiResponse.message)
            }

            ApiStatus.OnTimeoutException -> {
                showProgressBar(false)
                Snackbar.make(
                    getCustomView(),
                    getString(R.string.request_timeout_please_check_your_connection),
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(getString(R.string.retry), failureListener).show()
            }

            ApiStatus.OnUnknownHost, ApiStatus.OnConnectException -> {
                showProgressBar(false)
                baseActivity.onNetworkConnectionChanged(NetworkConnectivity.NetworkStatus.OnLost)
            }

            ApiStatus.OnSuccess -> showProgressBar(false)

            else -> {
            }
        }
    }

}