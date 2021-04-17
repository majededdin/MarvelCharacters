package majed.eddin.marvelcharacters.data.repository

import android.content.Context
import majed.eddin.marvelcharacters.data.consts.AppConst
import majed.eddin.marvelcharacters.utils.Utils.Companion.md5

open class GlobalRepo(context: Context) : BaseRepository(context) {

    fun getTimeStamp(): String = (System.currentTimeMillis() / 1000).toString()

    fun getApiKey(): String = AppConst.instance.publicKey

    fun getHash(): String =
        md5("${getTimeStamp()}${AppConst.instance.privateKey}${AppConst.instance.publicKey}")

}