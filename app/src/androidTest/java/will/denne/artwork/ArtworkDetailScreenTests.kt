package will.denne.artwork

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class ArtworkDetailScreenTests {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun detailViewHasContentDescription() {
        composeTestRule.waitUntilExactlyOneExists(hasText("Number 19"))
        composeTestRule.onNodeWithText("Number 19").performClick()
        composeTestRule.waitUntilExactlyOneExists(hasText("Number 19"))
        composeTestRule.onNodeWithTag("artworkImage").assertContentDescriptionEquals(
            "Blurry rectangles of yellow, gray, and black float against background of orange."
        )

    }
}