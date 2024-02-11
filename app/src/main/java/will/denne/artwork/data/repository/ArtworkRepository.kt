package will.denne.artwork.data.repository

import will.denne.artwork.data.api.ArticApi
import will.denne.artwork.data.model.ArtworkData
import will.denne.artwork.data.model.ArtworkDetailData

interface ArtworkRepository {
    suspend fun getArtworkDetail(id: Int): ArtworkDetailData
    suspend fun getArtworkList(pageNumber: Int): ArtworkData
    suspend fun searchArtwork(page: Int, query: String): ArtworkData
}

class ArtworkRepositoryImpl(
    private val api: ArticApi
): ArtworkRepository {
    override suspend fun getArtworkDetail(id: Int): ArtworkDetailData {
        return api.getArtworkDetails(
            id = id,
            fields = listOfArtworkDetailsFields.joinToString(",")
        ).data
    }

    override suspend fun getArtworkList(pageNumber: Int): ArtworkData {
        val queryListOfFields = listOfArtworkListFields.joinToString(",")
        return api.getArtwork(
            page = pageNumber,
            fields = queryListOfFields,
            size = PAGE_SIZE
        )
    }

    override suspend fun searchArtwork(page: Int, query: String): ArtworkData {
        return api.searchArtworks(
            page = page,
            query = query,
            fields = listOfArtworkListFields.joinToString( ","),
            size = PAGE_SIZE
        )
    }

    companion object {
        private const val PAGE_SIZE = 20
        private val listOfArtworkListFields = listOf("id", "title", "artist_display")
        private val listOfArtworkDetailsFields = listOf("id", "title", "artist_display", "image_id", "thumbnail", "on_loan_display", "date_display", "description")
    }
}
