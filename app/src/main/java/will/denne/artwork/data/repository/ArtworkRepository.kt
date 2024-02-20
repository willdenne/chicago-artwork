package will.denne.artwork.data.repository

import will.denne.artwork.data.NetworkResult
import will.denne.artwork.data.api.ArticApi
import will.denne.artwork.data.model.ArtworkData
import will.denne.artwork.data.model.ArtworkDetailData

interface ArtworkRepository {
    suspend fun getArtworkDetail(id: Int): NetworkResult<ArtworkDetailData>
    suspend fun getArtworkList(pageNumber: Int): NetworkResult<ArtworkData>
    suspend fun searchArtwork(page: Int, query: String): NetworkResult<ArtworkData>
}

class ArtworkRepositoryImpl(
    private val api: ArticApi
): ArtworkRepository {
    override suspend fun getArtworkDetail(id: Int): NetworkResult<ArtworkDetailData> {
        return try {
            val artwork = api.getArtworkDetails(
                id = id,
                fields = listOfArtworkDetailsFields.joinToString(",")
            )
            NetworkResult.Success(artwork.data)
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }

    override suspend fun getArtworkList(pageNumber: Int): NetworkResult<ArtworkData> {
        val queryListOfFields = listOfArtworkListFields.joinToString(",")
        return try {
            val artwork = api.getArtwork(
                page = pageNumber,
                fields = queryListOfFields,
                size = PAGE_SIZE
            )
            NetworkResult.Success(artwork)
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }

    override suspend fun searchArtwork(page: Int, query: String): NetworkResult<ArtworkData> {
        return try {
            val artwork = api.searchArtworks(
                page = page,
                query = query,
                fields = listOfArtworkListFields.joinToString( ","),
                size = PAGE_SIZE
            )
            NetworkResult.Success(artwork)
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }

    companion object {
        private const val PAGE_SIZE = 20
        private val listOfArtworkListFields = listOf("id", "title", "artist_display")
        private val listOfArtworkDetailsFields = listOf("id", "title", "artist_display", "image_id", "thumbnail", "on_loan_display", "date_display", "description")
    }
}
