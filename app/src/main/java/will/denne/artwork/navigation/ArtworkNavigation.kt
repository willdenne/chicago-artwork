package will.denne.artwork.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import will.denne.artwork.ui.artwork.ArtworkScreen

// Don't need a navigate to function since it's only ever the start
fun NavGraphBuilder.artworkScreen(
    onArtworkSelected: (Int) -> Unit
) {
    composable(ARTWORK_ROUTE) {
        ArtworkScreen(onArtworkSelected)
    }
}

const val ARTWORK_ROUTE = "artwork"
