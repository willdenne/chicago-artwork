package will.denne.artwork.ui.artworkdetail

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
                error = (uiState as ArtworkDetailScreenState.Error).error ?: stringResource(id = R.string.generic_error),
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
    val configuration = LocalConfiguration.current

    // Row if landscape, Column if portrait
    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            ImageContent(
                imageUrl = artwork.imageUrl,
                contentDescription = artwork.contentDescription,
                modifier = Modifier.fillMaxWidth(.5f)
            )
            ArtworkDescriptionContent(
                artwork = artwork,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ImageContent(
                imageUrl = artwork.imageUrl,
                contentDescription = artwork.contentDescription
            )
            ArtworkDescriptionContent(
                artwork = artwork
            )
        }
    }
}

@Composable
fun ImageContent(
    modifier: Modifier = Modifier,
    imageUrl: String?,
    contentDescription: String?
) {
    if (imageUrl != null) {
        AsyncImage(
            model = imageUrl,
            contentDescription = contentDescription,
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .testTag("artworkImage")
        )
    } else {
        Text(
            text = stringResource(R.string.no_image_available),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun ArtworkDescriptionContent(
    artwork: ArtworkDetailUiModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
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

@Preview
@Preview(
    device = "spec:orientation=landscape,width=350dp,height=800dp"
)
@Composable
fun ArtworkDetailContentPreview() {
    ArtworkDetailContent(
        artwork = ArtworkDetailUiModel(
            imageUrl = "https://www.artic.edu/iiif/2/04e42e38-be5d-dacf-d627-17b2ca320cbe/full/843,/0/default.jpg",
            contentDescription = "Artwork image",
            title = "…And the Home of the Brave",
            artist = "Charles Demuth\nAmerican, 1883–1935",
            date = "1931",
            onLoan = "On Loan",
            description = "Charles Demuth here portrayed a cigar factory using a sharply linear, planar style inspired by streamlined machinery. The building was part of the industrial landscape in his hometown of Lancaster, Pennsylvania, which Demuth began depicting with increasing monumentality in the last years of his life. Although he presented the factory with no reference to the potentially detrimental effects of industrialization, the painting expresses some irony or ambivalence. Demuth drew the title from the last line of “The Star-Spangled Banner,” which was adopted as the United States national anthem the year he painted this work, thus implying that for many workers, the factory was the new “home of the brave.”",
            id = 1
        )
    )
}
