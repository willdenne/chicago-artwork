package will.denne.artwork.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import will.denne.artwork.ui.artwork.ArtworkScreen
import will.denne.artwork.ui.artworkdetail.ArtworkDetailScreen

@Composable
fun NavigationComponent() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ARTWORK_ROUTE) {
        composable(ARTWORK_ROUTE) {
            ArtworkScreen(navController = navController)
        }
        composable("$ARTWORK_DETAIL_ROUTE/{$ARTWORK_ID}") { backStackEntry ->
            val artworkId = backStackEntry.arguments?.getString("artworkId")
            ArtworkDetailScreen(artworkId = artworkId?.toInt())
        }
    }
}

const val ARTWORK_ROUTE = "artwork"
const val ARTWORK_DETAIL_ROUTE = "artwork_detail"
const val ARTWORK_ID = "artworkId"
