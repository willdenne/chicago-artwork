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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import will.denne.artwork.R
import will.denne.artwork.ui.shared.Error as ErrorComposable
import will.denne.artwork.ui.shared.Loading

@Composable
fun ArtworkScreen(
    uiState: ArtworkScreenState,
    searchText: String,
    onArtworkSelected: (Int) -> Unit,
    retry: () -> Unit,
    updateSearchText: (String) -> Unit,
    searchArtwork: () -> Unit,
    loadMoreArtwork: () -> Unit
) {
    when (uiState) {
        is ArtworkScreenState.Loading -> {
            Loading()
        }
        is ArtworkScreenState.Error -> {
            ErrorComposable(
                error = uiState.error ?: stringResource(R.string.generic_error),
                retry = retry
            )
        }
        is ArtworkScreenState.Empty -> {
            Column {
                SearchBox(
                    searchText = searchText,
                    updateSearchText = updateSearchText,
                    onSearchClicked = searchArtwork
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
            ArtworkSuccessContent(
                uiState = uiState,
                onArtworkSelected = onArtworkSelected,
                searchText = searchText,
                updateSearchText = updateSearchText,
                searchArtwork = searchArtwork,
                loadMoreArtwork = loadMoreArtwork
            )
        }
    }
}

// Made this a separate composable so for readability
@Composable
fun ArtworkSuccessContent(
    uiState: ArtworkScreenState.Success,
    onArtworkSelected: (Int) -> Unit,
    loadMoreArtwork: () -> Unit,
    updateSearchText: (String) -> Unit,
    searchArtwork: () -> Unit,
    searchText: String
) {
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
            updateSearchText = updateSearchText,
            onSearchClicked = searchArtwork
        )
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(uiState.artwork) { art ->
                ArtItem(art, onArtworkSelected)
            }
            item {
                LaunchedEffect(
                    endOfListReached
                ) {
                    if ( !uiState.hasLoadedLastPage ) {
                        loadMoreArtwork()
                    }
                }
            }
            if (uiState.hasLoadedLastPage) {
                item {
                    Text(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = stringResource(R.string.end_of_list),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                item {
                    Loading()
                }
            }
        }
    }
}

@Composable
fun ArtItem(
    art: ArtworkUiModel,
    onArtworkSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onArtworkSelected(art.id)
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
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextField(
            value = searchText,
            onValueChange = updateSearchText,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 4.dp)
                .background(MaterialTheme.colorScheme.background, RoundedCornerShape(100))
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
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClicked()
                }
            )
        )
    }
}

@PreviewScreenSizes
@PreviewFontScale
@Composable
fun ArtworkSuccessContentPreview() {
    ArtworkSuccessContent(
        uiState = ArtworkScreenState.Success(
            artwork = List(20) {
                ArtworkUiModel(
                    id = 1,
                    title = "…And the Home of the Brave",
                    artist = "Charles Demuth\nAmerican, 1883–1935"
                )
            },
            hasLoadedLastPage = false
        ),
        onArtworkSelected = {},
        loadMoreArtwork = {},
        updateSearchText = {},
        searchArtwork = {},
        searchText = ""
    )
}
