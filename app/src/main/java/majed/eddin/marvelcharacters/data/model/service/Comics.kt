package majed.eddin.marvelcharacters.data.model.service

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Comics(
    var available: Int,
    var collectionURI: String,
    var returned: Int
) : Parcelable