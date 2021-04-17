package majed.eddin.marvelcharacters.data.remote

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.QueryMap

interface ApiService {

    @GET("v1/public/characters")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun getCharacters(
        @QueryMap map: HashMap<String, Any>
    ): ResponseBody
}