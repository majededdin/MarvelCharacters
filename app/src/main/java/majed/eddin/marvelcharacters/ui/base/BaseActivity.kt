package majed.eddin.marvelcharacters.ui.base

import am.networkconnectivity.NetworkConnectivity
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import majed.eddin.marvelcharacters.ui.view.fragments.LoadingFragment
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import majed.eddin.marvelcharacters.R
import majed.eddin.marvelcharacters.data.consts.AppConst
import majed.eddin.marvelcharacters.data.local.LocaleHelper
import majed.eddin.marvelcharacters.data.local.Prefs
import majed.eddin.marvelcharacters.data.model.modified.ErrorHandler
import majed.eddin.marvelcharacters.data.remote.ApiResponse
import majed.eddin.marvelcharacters.data.remote.ApiStatus
import majed.eddin.marvelcharacters.ui.viewModel.BaseViewModel
import majed.eddin.marvelcharacters.utils.BaseSnackBar
import majed.eddin.marvelcharacters.utils.Utils.Companion.errorResponseHandler
import majed.eddin.marvelcharacters.utils.extentionUtils.toGone
import majed.eddin.marvelcharacters.utils.extentionUtils.toVisible
import org.json.JSONException
import org.json.JSONObject

abstract class BaseActivity<V : BaseViewModel> : AppCompatActivity() {

    private var viewModel: V? = null
    private lateinit var layout: RelativeLayout

    private var secondRun = false
    private lateinit var progressLoading: ProgressBar
    private lateinit var layoutNetworkStatus: LinearLayout
    private lateinit var txtNetworkStatus: MaterialTextView

    private val loadingFragment: LoadingFragment = LoadingFragment()


    protected abstract fun getViewModel(): Class<V>?


    protected abstract fun viewModelInstance(viewModel: V?)


    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        handleNetworkResponse()
        viewModelInstance(initViewModel())
        saveDeviceLanguage()

        if (LocaleHelper.getLanguage(this) == "ar")
            LocaleHelper.setLocale(this, "ar")
        else LocaleHelper.setLocale(this, "en")

        super.onCreate(savedInstanceState)
    }


    private fun saveDeviceLanguage() {
        CoroutineScope(Dispatchers.IO).launch {
            val prefs = Prefs(applicationContext)
            if (AppConst.instance.deviceCurrentLanguage == "ar")
                prefs.setDeviceLanguage("ar")
            else
                prefs.setDeviceLanguage("en")
        }
    }


    abstract fun updateView()


    protected abstract fun setErrorHandler(handler: ErrorHandler)


    protected abstract fun viewInit()


    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            super.onBackPressed()
            finish()
        } else
            supportFragmentManager.popBackStack()
    }


    private fun initViewModel(): V? {
        if (getViewModel() != null)
            viewModel = ViewModelProvider(
                this, ViewModelProvider.AndroidViewModelFactory.getInstance(
                    application
                )
            ).get(getViewModel()!!)
        return viewModel
    }


    @SuppressLint("InflateParams")
    override fun setContentView(view: View?) {
        layout = layoutInflater.inflate(R.layout.activity_base, null) as RelativeLayout
        val coordinatorLayout: CoordinatorLayout = layout.findViewById(R.id.containerBase)
        baseInit()

        coordinatorLayout.addView(
            view, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )

        super.setContentView(layout)
    }


    private fun baseInit() {
        loadingFragment.isCancelable = false
        progressLoading = layout.findViewById(R.id.progress_loading)
        txtNetworkStatus = layout.findViewById(R.id.txt_networkStatus)
        layoutNetworkStatus = layout.findViewById(R.id.layout_networkStatus)
    }


    protected open fun initSwipeRefresh(swipeRefreshLayout: SwipeRefreshLayout) {
        swipeRefreshLayout.setColorSchemeColors(
            ContextCompat.getColor(this, R.color.colorPrimary),
            ContextCompat.getColor(this, R.color.colorPrimaryVariant),
            ContextCompat.getColor(this, R.color.colorSecondary)
        )
        swipeRefreshLayout.setOnRefreshListener { updateView() }
    }


    fun setLoadingFragmentStatus(status: Boolean) = showLoadingFragment(status)


    private fun showLoadingFragment(status: Boolean) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                if (!status && !loadingFragment.isDetached) loadingFragment.dismiss()
                else if (!loadingFragment.isAdded) loadingFragment.show(
                    supportFragmentManager, javaClass.name
                )
            } catch (ignored: IllegalStateException) {
            }
        }
    }


    open fun getCustomView(): View = findViewById(android.R.id.content)


    open fun getDeviceCurrentLanguage(): String = AppConst.instance.deviceCurrentLanguage


    open fun showLog(message: String) {
        if (AppConst.instance.isDebug)
            Log.e("showLog ", message)
    }


    open fun showMessage(message: String?) =
        Snackbar.make(getCustomView(), message!!, BaseTransientBottomBar.LENGTH_SHORT).show()


    open fun showMessage(message: String, callback: BaseTransientBottomBar.BaseCallback<Snackbar>) =
        Snackbar.make(getCustomView(), message, BaseTransientBottomBar.LENGTH_SHORT)
            .addCallback(callback).show()


    open fun failureMessage(failureMessage: String, listener: View.OnClickListener) {
        BaseSnackBar.make(getCustomView() as ViewGroup)
            .setText(getString(R.string.sorry_something_went_wrong_and_being_reported_now))
            .setDismiss()
            .setAction(getString(R.string.retry), listener)
            .addCallback(object : BaseTransientBottomBar.BaseCallback<BaseSnackBar>() {
                override fun onShown(transientBottomBar: BaseSnackBar?) {
                    super.onShown(transientBottomBar)
                    Log.e("failureMessage ", failureMessage)
                }
            }).show()
    }


    fun changeStatusBarColor(color: Int) {
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, color)
    }


    fun changeStatusBarColor(color: String) {
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.parseColor(color)
    }


    private fun refreshPageDetails() {
        updateView()
        for (fragment in supportFragmentManager.fragments) {
            if (fragment is BaseFragment<*>)
                fragment.updateView()
        }
    }


    open fun getResponseErrors(jsonObject: JSONObject) {
        val iterator = jsonObject.keys()
        while (iterator.hasNext()) {
            val key = iterator.next()
            var errorValue: String? = null
            try {
                errorValue = errorResponseHandler(jsonObject.getJSONArray(key))
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            setErrorHandler(ErrorHandler(key, errorValue!!))
            for (fragment in supportFragmentManager.fragments) {
                if (fragment is BaseFragment<*>)
                    fragment.setErrorHandler(ErrorHandler(key, errorValue))
            }
        }
    }


    fun handleApiResponse(apiResponse: ApiResponse<*>, failureListener: View.OnClickListener) {
        when (apiResponse.apiStatus) {

            ApiStatus.OnBackEndError -> {
                showLoadingFragment(false)
                failureMessage(apiResponse.message, failureListener)
            }

            ApiStatus.OnError -> {
                showLoadingFragment(false)
                if (apiResponse.errorBodyAsJSON != null)
                    getResponseErrors(apiResponse.errorBodyAsJSON!!)
                else
                    showMessage(apiResponse.message)
            }

            ApiStatus.OnFailure -> {
                showLoadingFragment(false)
                Log.e("OnFailure ", apiResponse.message)
            }

            ApiStatus.OnForbidden -> {
                showLoadingFragment(false)
                Log.e("OnForbidden ", apiResponse.message)
            }

            ApiStatus.OnHttpException -> {
                showLoadingFragment(false)
                showMessage(apiResponse.message)
            }

            ApiStatus.OnLoading -> showLoadingFragment(true)

            ApiStatus.OnNotFound -> {
                showLoadingFragment(false)
                showMessage(apiResponse.message)
            }

            ApiStatus.OnTimeoutException -> {
                showLoadingFragment(false)
                Snackbar.make(
                    getCustomView(),
                    getString(R.string.request_timeout_please_check_your_connection),
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(getString(R.string.retry), failureListener).show()
            }

            ApiStatus.OnUnknownHost, ApiStatus.OnConnectException -> {
                showLoadingFragment(false)
                onNetworkConnectionChanged(NetworkConnectivity.NetworkStatus.OnLost)
            }

            ApiStatus.OnSuccess -> showLoadingFragment(false)

            else -> {
            }
        }
    }


    private fun handleNetworkResponse() {
        NetworkConnectivity(this).observe(this, { onNetworkConnectionChanged(it) })
    }


    open fun onNetworkConnectionChanged(status: NetworkConnectivity.NetworkStatus) {
        when (status) {
            NetworkConnectivity.NetworkStatus.OnConnected -> {
                if (secondRun) {
                    secondRun = false
                    txtNetworkStatus.text = getString(R.string.connection_is_back)
                    progressLoading.toGone()
                    layoutNetworkStatus.setBackgroundColor(
                        ContextCompat.getColor(this, R.color.colorNetworkConnected)
                    )

                    refreshPageDetails()
                    lifecycleScope.launch(Dispatchers.Main) {
                        delay(2000)
                        layoutNetworkStatus.toGone()
                    }
                }
            }

            NetworkConnectivity.NetworkStatus.OnWaiting -> {
                secondRun = true
                txtNetworkStatus.text = getString(R.string.waiting_for_connection)
                progressLoading.toVisible()
                layoutNetworkStatus.toVisible()
                layoutNetworkStatus.setBackgroundColor(
                    ContextCompat.getColor(this, R.color.colorNetworkWaiting)
                )
            }

            NetworkConnectivity.NetworkStatus.OnLost -> {
                secondRun = true
                txtNetworkStatus.text = getString(R.string.connection_is_lost)
                progressLoading.toGone()
                layoutNetworkStatus.toVisible()
                layoutNetworkStatus.setBackgroundColor(
                    ContextCompat.getColor(this, R.color.colorNetworkLost)
                )
            }
        }
    }

}