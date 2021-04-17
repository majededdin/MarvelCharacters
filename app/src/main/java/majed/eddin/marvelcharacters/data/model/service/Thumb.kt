package majed.eddin.marvelcharacters.data.model.service

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Thumb(
    var path: String,
    var extension: String
) : Parcelable