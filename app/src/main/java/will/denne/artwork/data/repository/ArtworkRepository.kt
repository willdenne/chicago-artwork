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
        val result = api.getArtworkDetails(
            id = id,
            fields = listOfArtworkDetailsFields.joinToString(",")
        ).getOrElse { throwable ->
            return NetworkResult.Error(throwable)
        }
        return NetworkResult.Success(result.data)
    }

    override suspend fun getArtworkList(pageNumber: Int): NetworkResult<ArtworkData> {
        return NetworkResult.Success(
            api.getArtwork(
                page = pageNumber,
                fields = listOfArtworkListFields.joinToString(","),
                size = PAGE_SIZE
            ).getOrElse { throwable ->
                return NetworkResult.Error(throwable)
            }
        )
    }

    override suspend fun searchArtwork(page: Int, query: String): NetworkResult<ArtworkData> {
        return NetworkResult.Success(
            api.searchArtworks(
                page = page,
                query = query,
                fields = listOfArtworkListFields.joinToString(","),
                size = PAGE_SIZE
            ).getOrElse { throwable ->
                return NetworkResult.Error(throwable)
            }
        )
    }

    companion object {
        private const val PAGE_SIZE = 20
        private val listOfArtworkListFields = listOf("id", "title", "artist_display")
        private val listOfArtworkDetailsFields = listOf("id", "title", "artist_display", "image_id", "thumbnail", "on_loan_display", "date_display", "description")
    }
}
