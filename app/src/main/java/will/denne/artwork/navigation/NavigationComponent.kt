package will.denne.artwork.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun NavigationComponent() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ARTWORK_ROUTE) {
        artworkScreen(
            onArtworkSelected = { artworkId ->
                navController.navigateToArtworkDetail(artworkId)
            }
        )
        artworkDetailScreen()
    }
}
