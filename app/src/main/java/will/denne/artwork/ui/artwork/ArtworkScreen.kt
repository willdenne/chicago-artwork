package will.denne.artwork.ui.artwork

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import org.koin.androidx.compose.koinViewModel
import will.denne.artwork.R
import will.denne.artwork.navigation.ARTWORK_DETAIL_ROUTE
import will.denne.artwork.ui.shared.Error as ErrorComposable
import will.denne.artwork.ui.shared.Loading
import will.denne.artwork.viewmodel.ArtworkViewModel

@Composable
fun ArtworkScreen(navController: NavController) {
    val viewModel: ArtworkViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val searchText by viewModel.searchText.collectAsState()
    when (uiState) {
        is ArtworkScreenState.Loading -> {
            Loading()
        }
        is ArtworkScreenState.Error -> {
            ErrorComposable(
                error = (uiState as ArtworkScreenState.Error).error.message,
                retry = viewModel::retry
            )
        }
        is ArtworkScreenState.Empty -> {
            Column {
                SearchBox(
                    searchText = searchText,
                    updateSearchText = viewModel::updateSearchText,
                    onSearchClicked = viewModel::searchArtwork
                )
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    textAlign = TextAlign.Center,
                    text = stringResource(R.string.no_artwork),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        is ArtworkScreenState.Success -> {
            val listState = rememberLazyListState()

            fun LazyListState.isScrolledToEnd(): Boolean {
                return layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 10
            }

            val scrollState = rememberLazyListState()

            val endOfListReached by remember {
                derivedStateOf {
                    scrollState.isScrolledToEnd()
                }
            }

            Column {
                SearchBox(
                    searchText = searchText,
                    updateSearchText = viewModel::updateSearchText,
                    onSearchClicked = viewModel::searchArtwork
                )
                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val artwork = (uiState as? ArtworkScreenState.Success)?.artwork
                    if (artwork != null) {
                        items(artwork) { art ->
                            ArtItem(art, navController)
                        }
                    }
                    item {
                        val hasLoadedLastPage = (uiState as? ArtworkScreenState.Success)?.hasLoadedLastPage == true
                        LaunchedEffect(
                            endOfListReached
                        ) {
                            if ( !hasLoadedLastPage ) {
                                viewModel.loadMoreArtwork()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ArtItem(
    art: ArtworkUiModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Card(
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("$ARTWORK_DETAIL_ROUTE/${art.id}")
            }
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            Column {
                Text(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .fillMaxWidth(),
                    text = art.title,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .fillMaxWidth(),
                    text = art.artist,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun SearchBox(
    searchText: String,
    updateSearchText: (String) -> Unit,
    onSearchClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(MaterialTheme.colorScheme.background, CircleShape),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextField(
            value = searchText,
            onValueChange = updateSearchText,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
                .testTag("searchBox"),
            placeholder = {
                Text(text = stringResource(R.string.search_ellipsis))
            },
            trailingIcon = {
                IconButton(
                    modifier = Modifier.testTag("searchButton"),
                    onClick = onSearchClicked
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        tint = MaterialTheme.colorScheme.onBackground,
                        contentDescription = stringResource(R.string.search)
                    )
                }
            }
        )
    }
}
