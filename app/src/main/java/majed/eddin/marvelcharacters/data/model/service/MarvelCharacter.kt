package majed.eddin.marvelcharacters.data.model.service

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MarvelCharacter(
    var id: Int,
    var name: String,
    var description: String,
    var modified: String,
    var thumbnail: Thumb,
    var resourceURI: String,
    var comics: Comics,
    var series: Comics,
    var stories: Comics,
    var events: Comics
) : Parcelable {

    fun getFullImagePath(): String = thumbnail.path.plus(".".plus(thumbnail.extension))

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MarvelCharacter

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }

    override fun toString(): String {
        return "MarvelCharacter(id=$id, name='$name', description=$description, modified=$modified, thumbnail=$thumbnail, resourceURI=$resourceURI, comics=$comics, series=$series, stories=$stories, events=$events)"
    }
}