package will.denne.artwork

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test

class ArtworkScreenTests {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun searchBarReflectsInputText() {
        composeTestRule.waitUntilExactlyOneExists(hasText("Number 19"))
        composeTestRule.onNodeWithTag("searchBox").performTextInput("Charles Demuth")
        composeTestRule.onNodeWithTag("searchButton").performClick()
        composeTestRule.waitUntilExactlyOneExists(hasText("Still Life: Apples and Green Glass"))
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun searchingForInvalidArtworkShowsNoResults() {
        composeTestRule.waitUntilExactlyOneExists(hasText("Number 19"))
        composeTestRule.onNodeWithTag("searchBox").performTextInput("invalidsearchterm")
        composeTestRule.onNodeWithTag("searchButton").performClick()
        composeTestRule.waitUntilExactlyOneExists(hasText("No artwork matched your query, please try another search."))
    }
}