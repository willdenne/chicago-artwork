package will.denne.artwork.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.koin.androidx.compose.koinViewModel
import will.denne.artwork.ui.artwork.ArtworkScreen
import will.denne.artwork.viewmodel.ArtworkViewModel

// Don't need a navigate to function since it's only ever the start
fun NavGraphBuilder.artworkScreen(
    onArtworkSelected: (Int) -> Unit
) {
    composable(ARTWORK_ROUTE) {
        val viewModel: ArtworkViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsState()
        val searchText by viewModel.searchText.collectAsState()
        ArtworkScreen(
            uiState = uiState,
            searchText = searchText,
            onArtworkSelected = onArtworkSelected,
            retry = viewModel::retry,
            updateSearchText = viewModel::updateSearchText,
            searchArtwork = viewModel::searchArtwork,
            loadMoreArtwork = viewModel::loadMoreArtwork
        )
    }
}

const val ARTWORK_ROUTE = "artwork"
