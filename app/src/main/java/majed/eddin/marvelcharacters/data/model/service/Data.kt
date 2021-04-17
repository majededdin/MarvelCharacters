package majed.eddin.marvelcharacters.data.model.service

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Data(
    var offset: Int,
    var limit: Int,
    var total: Int,
    var count: Int,
    var results: ArrayList<MarvelCharacter>,
) : Parcelable {

    constructor() : this(1, 1, 1, 1, ArrayList())

    override fun toString(): String {
        return "Meta(offset=$offset, limit=$limit, total=$total, count='$count', results=$results)"
    }

}