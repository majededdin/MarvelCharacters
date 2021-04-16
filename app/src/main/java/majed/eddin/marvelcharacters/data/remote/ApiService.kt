package majed.eddin.marvelcharacters.data.remote

import majed.eddin.marvelcharacters.data.consts.Params.Companion.API_KEY
import majed.eddin.marvelcharacters.data.consts.Params.Companion.HASH
import majed.eddin.marvelcharacters.data.consts.Params.Companion.OFFSET
import majed.eddin.marvelcharacters.data.consts.Params.Companion.TIMESTAMP
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {

    @GET("v1/public/characters")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun getCharacters(
        @Query(TIMESTAMP) ts: String,
        @Query(API_KEY) apiKey: String,
        @Query(HASH) hash: String,
        @Query(OFFSET) pageIndex: Int
    ): ResponseBody
}