package majed.eddin.marvelcharacters.data.consts

import android.app.Application

class AppConst {

    var isDebug = false
    lateinit var appInstance: Application
    lateinit var appArFont: String
    lateinit var appEnFont: String
    lateinit var deviceCurrentLanguage: String
    lateinit var appBaseUrl: String
    lateinit var appWebSiteUrl: String
    lateinit var publicKey: String
    lateinit var privateKey: String

    companion object {
        var instance = AppConst()
    }

    fun getDefaultFontPath(): String {
        return if (deviceCurrentLanguage == "ar") appArFont else appEnFont
    }

}