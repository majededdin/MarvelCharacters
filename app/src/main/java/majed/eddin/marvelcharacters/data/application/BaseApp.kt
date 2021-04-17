package majed.eddin.marvelcharacters.data.application

import android.app.Application
import android.content.Context
import majed.eddin.marvelcharacters.BuildConfig
import majed.eddin.marvelcharacters.data.consts.AppConst
import majed.eddin.marvelcharacters.data.local.LocaleHelper
import java.util.*

class BaseApp : Application() {

    private lateinit var appConst: AppConst

    companion object {
        var instance: BaseApp = BaseApp()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        appConst = AppConst.instance
        initAppConst()
    }


    override fun attachBaseContext(base: Context?) {
        when (Locale.getDefault().language) {
            "ar" -> super.attachBaseContext(LocaleHelper.onAttach(base!!, "ar"))

            "en" -> super.attachBaseContext(LocaleHelper.onAttach(base!!, "ar"))

            else -> super.attachBaseContext(LocaleHelper.onAttach(base!!, "en"))
        }
    }


    private fun initAppConst() {
        appConst.appInstance = this
        appConst.isDebug = BuildConfig.DEBUG
        appConst.publicKey = "5d282f67fe01bdc5ee0cf9cbb3115ecc"
        appConst.privateKey = "98bc4e3ab7d0de05045d61f7e8fa6dc8be5fa8e5"
        appConst.appBaseUrl = BuildConfig.BASE_URL
        appConst.appWebSiteUrl = "https://gateway.marvel.com/"
        appConst.deviceCurrentLanguage = LocaleHelper.getLanguage(applicationContext)
    }

}