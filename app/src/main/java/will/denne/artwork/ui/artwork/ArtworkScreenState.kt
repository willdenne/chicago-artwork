package will.denne.artwork.ui.artwork

import will.denne.artwork.data.model.ArtworkListItemData

sealed class ArtworkScreenState {
    data object Loading: ArtworkScreenState()
    data class Success(
        val artwork: List<ArtworkUiModel>,
        val hasLoadedLastPage: Boolean
    ): ArtworkScreenState()
    data class Error(val error: Exception): ArtworkScreenState()
    data object Empty: ArtworkScreenState()
}

data class ArtworkUiModel(
    val id: Int,
    val title: String,
    val artist: String
) {

    companion object {
        fun fromArtworkData(artworkData: ArtworkListItemData): ArtworkUiModel = ArtworkUiModel(
            id = artworkData.id,
            title = artworkData.title,
            artist = artworkData.artist
        )
    }
}
