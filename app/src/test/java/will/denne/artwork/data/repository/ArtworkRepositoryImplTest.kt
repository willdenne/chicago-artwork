package will.denne.artwork.data.repository

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import will.denne.artwork.data.api.ArticApi
import will.denne.artwork.data.model.ArtworkDetail
import will.denne.artwork.data.model.artWorkDataBuilder
import will.denne.artwork.data.model.artWorkDetailDataBuilder
import will.denne.artwork.util.MainDispatcherRule

class ArtworkRepositoryImplTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var api: ArticApi
    private lateinit var repository: ArtworkRepositoryImpl

    @Before
    fun setUp() {
        api = mockk()
        coEvery {
            api.getArtwork(
                any(),
                any(),
                any()
            )
        } returns Result.success(artWorkDataBuilder())
        coEvery {
            api.searchArtworks(
                any(),
                any(),
                any(),
                any()
            )
        } returns Result.success(artWorkDataBuilder())
        coEvery {
            api.getArtworkDetails(
                any(),
                any()
            )
        } returns Result.success(ArtworkDetail(artWorkDetailDataBuilder()))
        repository = ArtworkRepositoryImpl(api)
    }

    @Test
    fun `getArtworkList calls api with expected parameters`() = runTest {
        repository.getArtworkList(1)
        coVerify {
            api.getArtwork(
                page = 1,
                fields = "id,title,artist_display",
                size = 20
            )
        }
    }

    @Test
    fun `searchArtwork calls api with expected parameters`() = runTest {
        repository.searchArtwork(1, "query")
        coVerify {
            api.searchArtworks(
                page = 1,
                query = "query",
                fields = "id,title,artist_display",
                size = 20
            )
        }
    }

    @Test
    fun `getArtworkDetail calls api with expected parameters`() = runTest {
        repository.getArtworkDetail(1)
        coVerify {
            api.getArtworkDetails(
                id = 1,
                fields = "id,title,artist_display,image_id,thumbnail,on_loan_display,date_display,description"
            )
        }
    }
}