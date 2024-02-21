package will.denne.artwork.ui.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import will.denne.artwork.R
import will.denne.artwork.ui.shared.theme.Red
import will.denne.artwork.ui.shared.theme.White

// Intro loading logo to try to copy the Art Institute of Chicago logo
@Composable
fun LoadingLogo(
    modifier: Modifier = Modifier
) {
    val textList = listOf(
        stringResource(id = R.string.chicago),
        stringResource(id = R.string.art),
        stringResource(id = R.string.institute)
    )
    val loadingContentDesc = stringResource(R.string.loading)
    Box(
        modifier = modifier
            .semantics {
                this.contentDescription = loadingContentDesc
            }
            .background(Red)
            .size(200.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        Column(
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
        ) {
            textList.forEachIndexed { index, text ->
                AnimatedText(text = text)
                if (index < textList.size - 1) Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Composable
fun AnimatedText(text: String) {
    val displayText = remember { mutableStateOf("") }

    LaunchedEffect(key1 = text) {
        launch {
            for (char in text) {
                delay(250)
                displayText.value += char
            }
        }
    }

    Text(
        text = displayText.value,
        style = MaterialTheme.typography.bodyLarge,
        fontSize = 24.sp,
        color = White
    )
}
