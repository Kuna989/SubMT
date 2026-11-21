package pl.submt

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pl.submt.ui.SubMtApp
import pl.submt.ui.theme.SubMtTheme

@RunWith(AndroidJUnit4::class)
class SubMtAppTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun startScreen_showsAppNameAndAccessibleLogo() {
        composeRule.setContent {
            SubMtTheme { SubMtApp() }
        }

        composeRule.onNodeWithText(context.getString(R.string.app_name)).assertIsDisplayed()
        composeRule.onNodeWithContentDescription(context.getString(R.string.app_logo_description))
            .assertIsDisplayed()
    }
}
