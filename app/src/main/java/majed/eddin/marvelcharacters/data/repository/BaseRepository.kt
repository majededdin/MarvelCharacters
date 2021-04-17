package majed.eddin.marvelcharacters.data.repository

import android.content.Context
import kotlinx.coroutines.flow.first
import majed.eddin.marvelcharacters.data.local.Prefs
import majed.eddin.marvelcharacters.data.remote.ApiClient
import majed.eddin.marvelcharacters.data.remote.ApiResponse

open class BaseRepository(context: Context) {

    internal var prefs: Prefs = Prefs(context)
    protected var apiService = ApiClient.getInstance()

    //---------------------------------------- ApiResponse Methods ---------------------------------

    protected fun <M> getApiError(throwable: Throwable) = ApiResponse<M>().getErrorBody(throwable)

    //---------------------------------------- Prefs Methods ---------------------------------------

    internal suspend fun getDeviceLanguage(): String = prefs.deviceLanguage.first()

}