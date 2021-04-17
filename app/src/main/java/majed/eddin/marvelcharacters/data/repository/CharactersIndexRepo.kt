package majed.eddin.marvelcharacters.data.repository

import android.app.Application
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import majed.eddin.marvelcharacters.data.consts.Params.Companion.API_KEY
import majed.eddin.marvelcharacters.data.consts.Params.Companion.DATA
import majed.eddin.marvelcharacters.data.consts.Params.Companion.HASH
import majed.eddin.marvelcharacters.data.consts.Params.Companion.NAME
import majed.eddin.marvelcharacters.data.consts.Params.Companion.OFFSET
import majed.eddin.marvelcharacters.data.consts.Params.Companion.TIMESTAMP
import majed.eddin.marvelcharacters.data.model.service.Data
import majed.eddin.marvelcharacters.data.remote.ApiResponse

class CharactersIndexRepo(application: Application) : GlobalRepo(application) {

    fun getCharactersIndex(name: String?, currentPage: Int) = flow {
//        emit(ApiResponse(ApiStatus.OnLoading))

        val map: HashMap<String, Any> = HashMap()
        map[TIMESTAMP] = getTimeStamp()
        map[API_KEY] = getApiKey()
        map[HASH] = getHash()
        map[OFFSET] = currentPage

        if (name != null)
            map[NAME] = name
        else
            map.remove(NAME)

        val advertsResponse = ApiResponse<Data>(
            apiService.getCharacters(map).string(), DATA, object : TypeToken<Data>() {}.type
        )

        emit(advertsResponse.getApiResult())
    }.catch { emit(getApiError(it)) }

}