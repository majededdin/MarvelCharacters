package majed.eddin.marvelcharacters.ui.base

import am.networkconnectivity.NetworkConnectivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import majed.eddin.marvelcharacters.R
import majed.eddin.marvelcharacters.data.consts.AppConst
import majed.eddin.marvelcharacters.data.remote.ApiResponse
import majed.eddin.marvelcharacters.data.remote.ApiStatus
import majed.eddin.marvelcharacters.ui.viewModel.BaseViewModel

abstract class BaseFragment<V : BaseViewModel> : Fragment() {

    private var viewModel: V? = null
    private lateinit var layout: FrameLayout
    lateinit var baseActivity: BaseActivity<*>
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout


    @LayoutRes
    protected abstract fun setLayout(): Int


    protected abstract fun getViewModel(): Class<V>?


    protected abstract fun viewModelInstance(viewModel: V?)


    abstract fun updateView()


    protected abstract fun viewInit()


    override fun onCreate(savedInstanceState: Bundle?) {
        viewModelInstance(initViewModel())
        super.onCreate(savedInstanceState)
    }


    private fun initViewModel(): V? {
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


    protected fun stopOnSwipeRefreshStatus() {
        swipeRefreshLayout.isEnabled = false
    }


    open fun getCustomView() = layout


    open fun getDeviceCurrentLanguage(): String = AppConst.instance.deviceCurrentLanguage


    open fun showLog(message: String) = baseActivity.showLog(message)


    open fun showMessage(message: String) = baseActivity.showMessage(message)


    open fun showMessage(message: String, callback: BaseTransientBottomBar.BaseCallback<Snackbar>) =
        baseActivity.showMessage(message, callback)


    open fun handleApiResponse(apiResponse: ApiResponse<*>, failureListener: View.OnClickListener) {
        when (apiResponse.apiStatus) {

            ApiStatus.OnBackEndError -> {
                baseActivity.failureMessage(apiResponse.message, failureListener)
            }

            ApiStatus.OnError ->
                showMessage(apiResponse.message)

            ApiStatus.OnFailure ->
                Log.e("OnFailure ", apiResponse.message)

            ApiStatus.OnForbidden ->
                Log.e("OnForbidden ", apiResponse.message)

            ApiStatus.OnHttpException ->
                showMessage(apiResponse.message)

            ApiStatus.OnNotFound ->
                showMessage(apiResponse.message)


            ApiStatus.OnTimeoutException -> {
                Snackbar.make(
                    getCustomView(),
                    getString(R.string.request_timeout_please_check_your_connection),
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(getString(R.string.retry), failureListener).show()
            }

            ApiStatus.OnUnknownHost, ApiStatus.OnConnectException -> {
                baseActivity.onNetworkConnectionChanged(NetworkConnectivity.NetworkStatus.OnLost)
            }

            else -> {
            }
        }
    }

}