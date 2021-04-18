package majed.eddin.marvelcharacters.data.repository

import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import majed.eddin.marvelcharacters.data.consts.Params
import majed.eddin.marvelcharacters.data.model.service.CharactersData
import majed.eddin.marvelcharacters.data.model.service.ComicsData
import majed.eddin.marvelcharacters.data.remote.ApiResponse
import majed.eddin.marvelcharacters.data.remote.ApiStatus

class CharacterDetailsRepo : GlobalRepo() {

    fun getCharacterDetails(characterId: String) = flow {
        emit(ApiResponse(ApiStatus.OnLoading))

        val map: HashMap<String, Any> = HashMap()
        map[Params.TIMESTAMP] = getTimeStamp()
        map[Params.API_KEY] = getApiKey()
        map[Params.HASH] = getHash()

        val characterResponse = ApiResponse<CharactersData>(
            apiService.getCharacterDetails(characterId, map).string(),
            Params.DATA,
            object : TypeToken<CharactersData>() {}.type
        )

        emit(characterResponse.getApiResult())
    }.catch { emit(getApiError(it)) }


    fun getCharacterChildDetails(characterId: String, type: String, currentPage: Int) = flow {
        val map: HashMap<String, Any> = HashMap()
        map[Params.TIMESTAMP] = getTimeStamp()
        map[Params.API_KEY] = getApiKey()
        map[Params.HASH] = getHash()
        map[Params.OFFSET] = currentPage

        val charactersResponse = ApiResponse<ComicsData>(
            apiService.getCharacterChildDetails(characterId, type, map).string(),
            Params.DATA,
            object : TypeToken<ComicsData>() {}.type
        )

        emit(charactersResponse.getApiResult())
    }.catch { emit(getApiError(it)) }
}