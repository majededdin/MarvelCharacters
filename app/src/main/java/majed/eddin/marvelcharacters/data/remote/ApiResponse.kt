package majed.eddin.marvelcharacters.data.remote

import android.util.Log
import com.google.gson.Gson
import majed.eddin.marvelcharacters.BuildConfig
import majed.eddin.marvelcharacters.data.consts.Params.Companion.Message
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.lang.reflect.Type
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ApiResponse<M> {

    var model: M? = null
    lateinit var message: String
    lateinit var apiStatus: ApiStatus

    private lateinit var parentJSON: JSONObject
    private lateinit var modelAsJSONObject: JSONObject


    //------------------------------------------ Public Constructor --------------------------------


    constructor()


    constructor(apiStatus: ApiStatus) {
        this.apiStatus = apiStatus
    }

    /**
     * Used when there is model returned from api
     * with specific key @param jsonKey
     */
    constructor(responseBodyAsString: String, jsonKey: String, tokenType: Type) {
        try {
            this.parentJSON = JSONObject(responseBodyAsString)
            showResponse(parentJSON.toString())
            this.message = parentJSON.optString(Message)
            if (parentJSON.has(jsonKey)) {
                try {
                    if (parentJSON.get(jsonKey) is JSONObject) {
                        this.modelAsJSONObject = parentJSON.getJSONObject(jsonKey)
                        this.model = getModelFromJSON(parentJSON.getJSONObject(jsonKey), tokenType)

                    }
                } catch (e: JSONException) {
                    showResponse("JSONException", e.message)
                }

            } else showResponse("JSONKey", "$jsonKey is not Exist...")

        } catch (throwable: Throwable) {
            showResponse("Throwable", throwable.message)
        }
    }


    //------------------------------------------ Public Methods ------------------------------------


    fun getApiResult(): ApiResponse<M> {
        return try {
            handleJsonResult()
        } catch (throwable: Throwable) {
            getErrorBody(throwable)
        }
    }


    //------------------------------------------ Private Methods -----------------------------------


    private fun handleJsonResult(): ApiResponse<M> {
        return when {
            model != null -> {
                if (message.isNotEmpty())
                    ApiResponse(ApiStatus.OnSuccess, model!!, message)
                else
                    ApiResponse(ApiStatus.OnSuccess, model!!)
            }

            message.isNotEmpty() -> ApiResponse(ApiStatus.OnSuccess, message)

            else -> ApiResponse(ApiStatus.OnSuccess)
        }
    }


    internal fun getErrorBody(throwable: Throwable): ApiResponse<M> {
        return when (throwable) {
            is HttpException -> {
                val throwableMessage = throwable.response()!!.errorBody()!!.string()
                val jsonObject = JSONObject(throwableMessage)
                when (throwable.code()) {
                    401 -> ApiResponse(ApiStatus.OnAuth)

                    403 -> {
                        showResponse("Forbidden", throwableMessage)
                        ApiResponse(ApiStatus.OnForbidden, jsonObject.optString(Message))
                    }

                    404 -> {
                        showResponse("Not Found", throwableMessage)
                        ApiResponse(ApiStatus.OnNotFound, jsonObject.optString(Message))
                    }

                    422 -> {
                        showResponse("errorBody", throwableMessage)
                        ApiResponse(ApiStatus.OnError, jsonObject.optString(Message))
                    }

                    500 -> {
                        showResponse("backEndException", throwableMessage)
                        ApiResponse(ApiStatus.OnBackEndError, throwableMessage)
                    }

                    else -> {
                        showResponse("HttpExceptionMsg", throwableMessage)
                        ApiResponse(ApiStatus.OnHttpException, throwableMessage)
                    }
                }
            }

            is UnknownHostException -> ApiResponse(
                ApiStatus.OnUnknownHost,
                throwable.message!!
            )

            is ConnectException -> ApiResponse(
                ApiStatus.OnConnectException,
                throwable.message!!
            )

            is SocketTimeoutException -> ApiResponse(
                ApiStatus.OnTimeoutException,
                throwable.message!!
            )

            else -> {
                val throwableMsg = throwable.message
                showResponse("throwableMsg", throwableMsg)
                ApiResponse(ApiStatus.OnFailure, throwableMsg!!)
            }
        }
    }


    //------------------------------------------ Private Setting Methods ---------------------------

    private fun showResponse(message: String?) {
        if (BuildConfig.DEBUG)
            Log.e("ApiResponse ", "JSONResponse: $message")
    }


    private fun showResponse(key: String, message: String?) {
        if (BuildConfig.DEBUG)
            Log.e("ApiResponse ", "$key: $message")
    }

    //------------------------------------------ Private Parsing Methods ---------------------------


    private fun getModelFromJSON(jsonObject: JSONObject, tokenType: Type): M =
        Gson().fromJson(jsonObject.toString(), tokenType)


    //------------------------------------------ Private Constructor --------------------------------


    private constructor(apiStatus: ApiStatus, message: String) {
        this.apiStatus = apiStatus
        this.message = message
    }


    private constructor(apiStatus: ApiStatus, model: M) {
        this.apiStatus = apiStatus
        this.model = model
    }


    private constructor(apiStatus: ApiStatus, model: M, message: String) {
        this.apiStatus = apiStatus
        this.model = model
        this.message = message
    }

}