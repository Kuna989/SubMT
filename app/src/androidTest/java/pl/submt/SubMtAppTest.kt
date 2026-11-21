package pl.submt

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Smoke test on a real device / emulator: launches [MainActivity] (with Hilt and Room)
 * and walks through the top-level screens. Detailed navigation tests live in `src/test`.
 */
@RunWith(AndroidJUnit4::class)
class SubMtAppTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private fun string(resId: Int) = composeRule.activity.getString(resId)

    @Test
    fun appLaunches_andTopLevelScreensAreReachable() {
        composeRule.onNodeWithText(string(R.string.subscriptions_empty)).assertIsDisplayed()

        listOf(R.string.nav_calendar, R.string.nav_statistics, R.string.nav_settings).forEach { label ->
            composeRule.onNode(hasText(string(label)) and hasClickAction()).performClick()
            composeRule.onNodeWithText(string(R.string.placeholder_under_construction)).assertIsDisplayed()
        }
    }
}
