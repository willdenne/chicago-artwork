package will.denne.artwork.ui.artworkdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import coil.compose.AsyncImage
import com.google.android.material.textview.MaterialTextView
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import will.denne.artwork.R
import will.denne.artwork.ui.shared.Error
import will.denne.artwork.ui.shared.Loading
import will.denne.artwork.viewmodel.ArtworkDetailViewModel

@Composable
fun ArtworkDetailScreen(artworkId: Int?) {
    val viewModel: ArtworkDetailViewModel = koinViewModel { parametersOf(artworkId) }
    val uiState by viewModel.uiState.collectAsState()
    when (uiState) {
        is ArtworkDetailScreenState.Loading -> {
            Loading()
        }

        is ArtworkDetailScreenState.Error -> {
            Error(
                error = (uiState as ArtworkDetailScreenState.Error).errorMessage,
                retry = viewModel::retry
            )
        }

        is ArtworkDetailScreenState.Success -> {
            ArtworkDetailContent(artwork = (uiState as ArtworkDetailScreenState.Success).artwork)
        }
    }
}

@Composable
fun ArtworkDetailContent(artwork: ArtworkDetailUiModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (artwork.imageUrl != null) {
            AsyncImage(
                model = artwork.imageUrl,
                contentDescription = artwork.contentDescription,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .testTag("artworkImage")
            )
        } else {
            Text(
                text = stringResource(R.string.no_image_avalable),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }
        Column(
            Modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                text = artwork.title,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                text = artwork.artist,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                text = artwork.date,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
            HTMLText(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                text = artwork.onLoan
            )
            HTMLText(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                text = artwork.description
            )
        }
    }
}

// Jetpack Compose still doesn't support HTML tags
@Composable
fun HTMLText(
    text: String,
    modifier: Modifier = Modifier
) {
    val spannedText = HtmlCompat.fromHtml(text, 0)
    AndroidView(
        modifier = modifier,
        factory = {
            MaterialTextView(it).apply {
                textAlignment = MaterialTextView.TEXT_ALIGNMENT_CENTER
            }
        },
        update = { it.text = spannedText }
    )
}
