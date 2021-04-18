package majed.eddin.marvelcharacters.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import java.util.*

class LocaleHelper {

    companion object {
        private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"

        fun onAttach(context: Context, defaultLanguage: String): Context {
            val language = getPersistedData(context, defaultLanguage)
            return setLocale(context, language)
        }

        fun getLanguage(context: Context): String {
            return getPersistedData(context, Locale.getDefault().language)
        }

        fun setLocale(context: Context, language: String): Context {
            persist(context, language)
            return updateResourcesUp(context, language)
        }

        private fun getPersistedData(context: Context, defaultLanguage: String): String {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            return prefs.getString(SELECTED_LANGUAGE, defaultLanguage)!!
        }

        private fun persist(context: Context, language: String) {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            val editor: SharedPreferences.Editor = prefs.edit()

            editor.putString(SELECTED_LANGUAGE, language)
            editor.apply()
        }


        @Suppress("DEPRECATION")
        private fun updateResourcesUp(context: Context, language: String): Context {
            val activityRes = context.resources
            val activityConf = activityRes.configuration
            val newLocale = Locale(language)
            activityConf.setLocale(newLocale)
            activityConf.setLayoutDirection(newLocale)
            activityRes.updateConfiguration(activityConf, activityRes.displayMetrics)
            val applicationRes = context.resources
            val applicationConf = applicationRes.configuration
            applicationConf.setLocale(newLocale)
            applicationRes.updateConfiguration(applicationConf, applicationRes.displayMetrics)
            return context
        }

    }
}