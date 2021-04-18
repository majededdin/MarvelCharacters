package majed.eddin.marvelcharacters.data.repository

import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import majed.eddin.marvelcharacters.data.consts.Params.Companion.API_KEY
import majed.eddin.marvelcharacters.data.consts.Params.Companion.DATA
import majed.eddin.marvelcharacters.data.consts.Params.Companion.HASH
import majed.eddin.marvelcharacters.data.consts.Params.Companion.NAME
import majed.eddin.marvelcharacters.data.consts.Params.Companion.OFFSET
import majed.eddin.marvelcharacters.data.consts.Params.Companion.TIMESTAMP
import majed.eddin.marvelcharacters.data.model.service.CharactersData
import majed.eddin.marvelcharacters.data.remote.ApiResponse

class CharactersIndexRepo : GlobalRepo() {

    fun getCharactersIndex(name: String?, currentPage: Int) = flow {
        val map: HashMap<String, Any> = HashMap()
        map[TIMESTAMP] = getTimeStamp()
        map[API_KEY] = getApiKey()
        map[HASH] = getHash()
        map[OFFSET] = currentPage

        if (name != null)
            map[NAME] = name
        else
            map.remove(NAME)

        val charactersResponse = ApiResponse<CharactersData>(
            apiService.getCharacters(map).string(),
            DATA,
            object : TypeToken<CharactersData>() {}.type
        )

        emit(charactersResponse.getApiResult())
    }.catch { emit(getApiError(it)) }

}