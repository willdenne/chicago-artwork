package will.denne.artwork.ui.shared

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import will.denne.artwork.R

@Composable
fun Error(
    error: String?,
    retry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(top = 24.dp)
    ) {
        Text(
            text = stringResource(R.string.error),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp)
                .align(Alignment.CenterHorizontally)
                .wrapContentWidth(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center
        )
        Text(
            text = error ?: "",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp)
                .wrapContentWidth(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center
        )
        Button(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally),
            onClick = retry
        ) {
            Text(text = stringResource(R.string.retry))
        }
    }
}

@Preview
@Composable
fun ErrorPreview() {
    Error(
        error = "An error occurred",
        retry = {}
    )
}
