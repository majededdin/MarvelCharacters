package majed.eddin.marvelcharacters.utils

import android.util.Log
import majed.eddin.marvelcharacters.data.consts.AppConst
import org.json.JSONArray
import org.json.JSONException
import java.security.NoSuchAlgorithmException


class Utils {
    companion object {

        fun errorResponseHandler(arr: JSONArray): String {
            val errorKey = StringBuilder()
            for (i in 0 until arr.length()) {
                try {
                    if (AppConst.instance.isDebug)
                        Log.e("ErrorResponseHandler:", "arr " + arr.getString(i))
                    errorKey.append(arr[i])
                    if (i != arr.length() - 1) errorKey.append("\n")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            return errorKey.toString()
        }


        fun md5(s: String): String {
            val md5 = "MD5"
            try {
                // Create MD5 Hash
                val digest = java.security.MessageDigest
                    .getInstance(md5)
                digest.update(s.toByteArray())
                val messageDigest = digest.digest()

                // Create Hex String
                val hexString = StringBuilder()
                for (aMessageDigest in messageDigest) {
                    var h = Integer.toHexString(0xFF and aMessageDigest.toInt())
                    while (h.length < 2)
                        h = "0$h"
                    hexString.append(h)
                }
                return hexString.toString()

            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }

            return ""
        }

    }
}