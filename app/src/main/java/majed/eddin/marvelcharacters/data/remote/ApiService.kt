package majed.eddin.marvelcharacters.data.remote

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface ApiService {

    @GET("v1/public/characters")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun getCharacters(
        @QueryMap map: HashMap<String, Any>
    ): ResponseBody

    @GET("v1/public/characters/{characterId}")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun getCharacterDetails(
        @Path("characterId") characterId: String,
        @QueryMap map: HashMap<String, Any>
    ): ResponseBody

    @GET("v1/public/characters/{characterId}/{type}")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun getCharacterChildDetails(
        @Path("characterId") characterId: String,
        @Path("type") type: String,
        @QueryMap map: HashMap<String, Any>
    ): ResponseBody

}