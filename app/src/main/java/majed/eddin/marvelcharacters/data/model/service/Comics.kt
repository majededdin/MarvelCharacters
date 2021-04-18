package majed.eddin.marvelcharacters.data.model.service

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Comics(
    var available: Int?,
    var collectionURI: String?,
    var returned: Int?,
    var title: String?,
    var thumbnail: Thumb?,
    var items: ArrayList<ComicSummery?>?
) : Parcelable {

    fun getFullImagePath(): String? =
        if (thumbnail != null) thumbnail!!.path.plus(".".plus(thumbnail!!.extension)) else null

}