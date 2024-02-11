package will.denne.artwork.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArtworkDetail(
    val data: ArtworkDetailData
)

@Serializable
data class ArtworkDetailData(
    val id: Int,
    val title: String,
    @SerialName("artist_display")
    val artist: String,
    @SerialName("image_id")
    val imageId: String? = null,
    val thumbnail: Thumbnail? = null,
    @SerialName("on_loan_display")
    val onLoanDisplay: String? = null,
    @SerialName("date_display")
    val dateDisplay: String? = null,
    val description: String? = null
)

@Serializable
data class Thumbnail(
    @SerialName("alt_text")
    val altText: String
)
