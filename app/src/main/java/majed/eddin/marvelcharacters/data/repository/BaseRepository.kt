package majed.eddin.marvelcharacters.data.repository

import majed.eddin.marvelcharacters.data.remote.ApiClient
import majed.eddin.marvelcharacters.data.remote.ApiResponse

open class BaseRepository {

    protected var apiService = ApiClient.getInstance()

    //---------------------------------------- ApiResponse Methods ---------------------------------

    protected fun <M> getApiError(throwable: Throwable) = ApiResponse<M>().getErrorBody(throwable)

}