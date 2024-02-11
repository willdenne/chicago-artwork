package will.denne.artwork.ui.shared

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import will.denne.artwork.R

@Composable
fun Loading(
    modifier: Modifier = Modifier
) {
    val loadingContentDesc = stringResource(R.string.loading)
    CircularProgressIndicator(
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier
            .fillMaxSize()
            .semantics {
                this.contentDescription = loadingContentDesc
            }
            .padding(5.dp)
            .wrapContentWidth(Alignment.CenterHorizontally)
            .wrapContentHeight(Alignment.CenterVertically)
    )
}
