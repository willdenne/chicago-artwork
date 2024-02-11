package will.denne.artwork.ui.artworkdetail

import will.denne.artwork.data.model.ArtworkDetailData
import will.denne.artwork.util.ImageLinkGenerator

sealed class ArtworkDetailScreenState {
    data object Loading: ArtworkDetailScreenState()
    data class Success(val artwork: ArtworkDetailUiModel): ArtworkDetailScreenState()
    data class Error(val errorMessage: String): ArtworkDetailScreenState()
}

data class ArtworkDetailUiModel(
    val id: Int,
    val title: String,
    val artist: String,
    val date: String,
    val onLoan: String,
    val imageUrl: String?,
    val contentDescription: String,
    val description: String
) {

    companion object {
        fun fromArtworkData(artworkData: ArtworkDetailData): ArtworkDetailUiModel = ArtworkDetailUiModel(
            id = artworkData.id,
            title = artworkData.title,
            artist = artworkData.artist,
            imageUrl = ImageLinkGenerator.generateImageUrl(artworkData.imageId),
            contentDescription = artworkData.thumbnail?.altText ?: "",
            onLoan = artworkData.onLoanDisplay ?: "",
            date = artworkData.dateDisplay ?: "",
            description = artworkData.description ?: ""
        )
    }
}
