package will.denne.artwork.viewmodel

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import will.denne.artwork.data.model.artWorkDetailDataBuilder
import will.denne.artwork.data.repository.ArtworkRepository
import will.denne.artwork.ui.artworkdetail.ArtworkDetailScreenState
import will.denne.artwork.ui.artworkdetail.ArtworkDetailUiModel
import will.denne.artwork.util.MainDispatcherRule

class ArtworkDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `getArtworkDetail should set uiState to Success when successful`() = runTest {
        val artworkRepository = mockk<ArtworkRepository>()

        coEvery {
            artworkRepository.getArtworkDetail(any())
        } returns artWorkDetailDataBuilder()
        val viewModel = ArtworkDetailViewModel(1, artworkRepository)

        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(
                ArtworkDetailScreenState.Success(
                    ArtworkDetailUiModel(
                        id = 1,
                        title = "title",
                        artist = "artist",
                        imageUrl = "https://www.artic.edu/iiif/2/imageId/full/843,/0/default.jpg",
                        contentDescription = "thumbnail",
                        onLoan = "onLoanDisplay",
                        date = "dateDisplay",
                        description = "description"
                    )
                )
            )
        }
        coVerify {
            artworkRepository.getArtworkDetail(1)
        }
    }

    @Test
    fun `getArtworkDetail should set uiState to Error when unsuccessful`() = runTest {
        val artworkRepository = mockk<ArtworkRepository>()

        coEvery {
            artworkRepository.getArtworkDetail(any())
        } throws Exception("Error getting artwork details")
        val viewModel = ArtworkDetailViewModel(1, artworkRepository)

        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(
                ArtworkDetailScreenState.Error(
                    "Error getting artwork details"
                )
            )
        }
    }
}
