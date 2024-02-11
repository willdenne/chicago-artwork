package will.denne.artwork.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArtworkData(
    val data: List<ArtworkListItemData>,
    val pagination: Pagination
)

@Serializable
data class Pagination(
    @SerialName("total_pages")
    val totalPages: Int
)

@Serializable
data class ArtworkListItemData(
    val id: Int,
    val title: String,
    @SerialName("artist_display")
    val artist: String
)
