package majed.eddin.marvelcharacters.utils

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import majed.eddin.marvelcharacters.R
import java.security.NoSuchAlgorithmException
import java.util.regex.Matcher
import java.util.regex.Pattern


class Utils {
    companion object {

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


        fun highlight(context: Context, search: String, originalText: String): CharSequence {

            val spannableStringSearch = SpannableString(originalText)
            val pattern: Pattern = Pattern.compile(search, Pattern.CASE_INSENSITIVE)
            val matcher: Matcher = pattern.matcher(originalText)

            while (matcher.find()) {
                spannableStringSearch.setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(context, R.color.colorPrimary)
                    ),
                    matcher.start(), matcher.end(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            return spannableStringSearch
        }

    }
}