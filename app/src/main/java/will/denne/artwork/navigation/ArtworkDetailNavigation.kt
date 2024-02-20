package will.denne.artwork.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import will.denne.artwork.ui.artworkdetail.ArtworkDetailScreen
import will.denne.artwork.viewmodel.ArtworkDetailViewModel

fun NavController.navigateToArtworkDetail(artworkId: Int) = this.navigate("$ARTWORK_DETAIL_ROUTE/$artworkId")

fun NavGraphBuilder.artworkDetailScreen() {
    composable(
        route = "$ARTWORK_DETAIL_ROUTE/{$ARTWORK_ID_ARG}",
        arguments = listOf(navArgument(ARTWORK_ID_ARG) { type = NavType.IntType })
    ) { backStackEntry ->
        val viewModel: ArtworkDetailViewModel =
            koinViewModel { parametersOf(backStackEntry.arguments?.getInt(ARTWORK_ID_ARG) ?: 0) }
        val uiState by viewModel.uiState.collectAsState()
        ArtworkDetailScreen(
            uiState = uiState,
            retry = viewModel::retry
        )
    }
}

private const val ARTWORK_ID_ARG = "artworkId"
private const val ARTWORK_DETAIL_ROUTE = "artwork_detail"
