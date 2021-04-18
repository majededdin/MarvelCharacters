package majed.eddin.marvelcharacters.data.model.service

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ComicSummery(
    var resourceURI: String,
    var name: String
) : Parcelable