package will.denne.artwork.data.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import will.denne.artwork.data.model.ArtworkData
import will.denne.artwork.data.model.ArtworkDetail

interface ArticApi {

    @GET("/api/v1/artworks")
    suspend fun getArtwork(
        @Query("page") page: Int,
        @Query("fields") fields: String,
        @Query("limit") size: Int
    ): Result<ArtworkData>

    @GET("/api/v1/artworks/{id}")
    suspend fun getArtworkDetails(
        @Path("id") id: Int,
        @Query("fields") fields: String
    ): Result<ArtworkDetail>

    @GET("/api/v1/artworks/search")
    suspend fun searchArtworks(
        @Query("page") page: Int,
        @Query("q") query: String,
        @Query("fields") fields: String,
        @Query("limit") size: Int
    ): Result<ArtworkData>
}
