package majed.eddin.marvelcharacters.data.repository

import majed.eddin.marvelcharacters.data.consts.AppConst
import majed.eddin.marvelcharacters.utils.Utils.Companion.md5

open class GlobalRepo : BaseRepository() {

    fun getTimeStamp(): String = (System.currentTimeMillis() / 1000).toString()

    fun getApiKey(): String = AppConst.instance.publicKey

    fun getHash(): String =
        md5("${getTimeStamp()}${AppConst.instance.privateKey}${AppConst.instance.publicKey}")

}