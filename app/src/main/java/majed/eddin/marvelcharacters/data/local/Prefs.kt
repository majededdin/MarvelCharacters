package majed.eddin.marvelcharacters.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import okio.IOException

class Prefs(context: Context) {

    private val prefs = context.createDataStore(name = "UserPrefs")

    companion object {
        private val DEVICE_LANGUAGE = stringPreferencesKey("deviceLanguage")
    }


    //--------------------------------------------- Device Language --------------------------------

    val deviceLanguage: Flow<String> = prefs.data.catch { handleEmptyError(it) }
        .map { it[DEVICE_LANGUAGE] ?: "en" }

    suspend fun setDeviceLanguage(language: String) {
        prefs.edit { it[DEVICE_LANGUAGE] = language }
    }

    //--------------------------------------------- PrefsErrors ------------------------------------

    private fun handleEmptyError(it: Throwable) = flow {
        if (it is IOException) {
            it.printStackTrace()
            emit(emptyPreferences())
        } else
            throw it
    }

}