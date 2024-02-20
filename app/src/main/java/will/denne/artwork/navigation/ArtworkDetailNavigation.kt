package will.denne.artwork.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import will.denne.artwork.ui.artworkdetail.ArtworkDetailScreen

fun NavController.navigateToArtworkDetail(artworkId: Int) = this.navigate("$ARTWORK_DETAIL_ROUTE/$artworkId")

fun NavGraphBuilder.artworkDetailScreen() {
    composable(
        route = "$ARTWORK_DETAIL_ROUTE/{$ARTWORK_ID_ARG}",
        arguments = listOf(navArgument(ARTWORK_ID_ARG) { type = NavType.IntType })
        ) { backStackEntry ->
        ArtworkDetailScreen(
            artworkId = backStackEntry.arguments?.getInt(ARTWORK_ID_ARG) ?: 0
        )
    }
}

private const val ARTWORK_ID_ARG = "artworkId"
private const val ARTWORK_DETAIL_ROUTE = "artwork_detail"
