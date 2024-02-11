package will.denne.artwork.viewmodel

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import will.denne.artwork.data.model.artWorkDataBuilder
import will.denne.artwork.data.model.artworkListBuilder
import will.denne.artwork.data.model.paginationBuilder
import will.denne.artwork.data.repository.ArtworkRepository
import will.denne.artwork.ui.artwork.ArtworkScreenState
import will.denne.artwork.ui.artwork.ArtworkUiModel
import will.denne.artwork.util.MainDispatcherRule

class ArtworkViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `getArtworkDetail should set uiState to Success when successful, not last page`() = runTest {
        val artworkRepository = mockk<ArtworkRepository>()

        coEvery {
            artworkRepository.getArtworkList(any())
        } returns artWorkDataBuilder(
            pagination = paginationBuilder(
                totalPages = 2
            )
        )
        val viewModel = ArtworkViewModel(artworkRepository)

        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(
                ArtworkScreenState.Success(
                    listOf(
                        ArtworkUiModel(
                            id = 1,
                            title = "title",
                            artist = "artist"
                        )
                    ),
                    hasLoadedLastPage = false
                )
            )
        }
    }

    @Test
    fun `loading second final page sets hasLoadedLastPage to true`() = runTest {
        val artworkRepository = mockk<ArtworkRepository>()

        coEvery {
            artworkRepository.getArtworkList(any())
        } returns artWorkDataBuilder(
            pagination = paginationBuilder(
                totalPages = 2
            )
        )
        coEvery {
            artworkRepository.getArtworkList(2)
        } returns artWorkDataBuilder(
            data = listOf(
                artworkListBuilder(
                    id = 2
                )
            ),
            pagination = paginationBuilder(
                totalPages = 2
            )
        )
        val viewModel = ArtworkViewModel(artworkRepository)

        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(
                ArtworkScreenState.Success(
                    listOf(
                        ArtworkUiModel(
                            id = 1,
                            title = "title",
                            artist = "artist"
                        )
                    ),
                    hasLoadedLastPage = false
                )
            )
        }
        viewModel.loadMoreArtwork()
        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(
                ArtworkScreenState.Success(
                    listOf(
                        ArtworkUiModel(
                            id = 1,
                            title = "title",
                            artist = "artist"
                        ),
                        ArtworkUiModel(
                            id = 2,
                            title = "title",
                            artist = "artist"
                        )
                    ),
                    hasLoadedLastPage = true
                )
            )
        }
    }
}
