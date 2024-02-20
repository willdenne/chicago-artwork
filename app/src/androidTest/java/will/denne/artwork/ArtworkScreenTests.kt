package will.denne.artwork

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasTestTag
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
        composeTestRule.waitUntilExactlyOneExists(hasTestTag("searchBox"))
        composeTestRule.onNodeWithTag("searchBox").performTextInput("Still Life: Apples and Green")
        composeTestRule.onNodeWithTag("searchButton").performClick()
        composeTestRule.waitUntilExactlyOneExists(hasText("Still Life: Apples and Green Glass"))
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun searchingForInvalidArtworkShowsNoResults() {
        composeTestRule.waitUntilExactlyOneExists(hasTestTag("searchBox"))
        composeTestRule.onNodeWithTag("searchBox").performTextInput("invalidsearchterm")
        composeTestRule.onNodeWithTag("searchButton").performClick()
        composeTestRule.waitUntilExactlyOneExists(hasText("No artwork matched your query, please try another search."))
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun searchingForLimitedSearchResultsDisplaysText() {
        composeTestRule.waitUntilExactlyOneExists(hasTestTag("searchBox"))
        composeTestRule.onNodeWithTag("searchBox").performTextInput("number 19")
        composeTestRule.onNodeWithTag("searchButton").performClick()
        composeTestRule.waitUntilExactlyOneExists(hasText("That's it, you've seen all of the art."))
    }
}
