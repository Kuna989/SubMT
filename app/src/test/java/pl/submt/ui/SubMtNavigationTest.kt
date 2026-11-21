package pl.submt.ui

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pl.submt.R
import pl.submt.ui.navigation.CalendarRoute
import pl.submt.ui.navigation.SettingsRoute
import pl.submt.ui.navigation.StatisticsRoute
import pl.submt.ui.navigation.SubscriptionEditRoute
import pl.submt.ui.navigation.SubscriptionsRoute
import pl.submt.ui.theme.SubMtTheme
import kotlin.reflect.KClass

/** UI navigation tests, run on the JVM with Robolectric (no emulator needed). */
@RunWith(AndroidJUnit4::class)
class SubMtNavigationTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private lateinit var navController: TestNavHostController

    @Before
    fun setUp() {
        composeRule.setContent {
            navController = TestNavHostController(context).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }
            SubMtTheme {
                SubMtApp(navController = navController)
            }
        }
    }

    @Test
    fun app_startsOnSubscriptionList() {
        assertCurrentRoute(SubscriptionsRoute::class)
        composeRule.onNodeWithText(string(R.string.subscriptions_empty)).assertIsDisplayed()
    }

    @Test
    fun navigationBar_opensEveryTopLevelScreen() {
        clickNavItem(R.string.nav_calendar)
        assertCurrentRoute(CalendarRoute::class)

        clickNavItem(R.string.nav_statistics)
        assertCurrentRoute(StatisticsRoute::class)

        clickNavItem(R.string.nav_settings)
        assertCurrentRoute(SettingsRoute::class)

        clickNavItem(R.string.nav_subscriptions)
        assertCurrentRoute(SubscriptionsRoute::class)
    }

    @Test
    fun switchingTabs_doesNotGrowBackStack_backReturnsToStart() {
        clickNavItem(R.string.nav_calendar)
        clickNavItem(R.string.nav_statistics)

        composeRule.runOnIdle { navController.popBackStack() }

        assertCurrentRoute(SubscriptionsRoute::class)
    }

    @Test
    fun addButton_opensFormWithoutNavigationBar_andUpReturnsToList() {
        // Found by its accessibility label, which TalkBack reads out.
        composeRule.onNodeWithContentDescription(string(R.string.action_add_subscription))
            .performClick()

        assertCurrentRoute(SubscriptionEditRoute::class)
        composeRule.onNodeWithText(string(R.string.nav_calendar)).assertDoesNotExist()

        composeRule.onNodeWithContentDescription(string(R.string.action_navigate_up)).performClick()

        assertCurrentRoute(SubscriptionsRoute::class)
        composeRule.onNodeWithText(string(R.string.nav_calendar)).assertIsDisplayed()
    }

    @Test
    fun editRoute_forExistingSubscription_showsEditTitle() {
        composeRule.runOnIdle { navController.navigate(SubscriptionEditRoute(subscriptionId = 7)) }

        composeRule.onNodeWithText(string(R.string.title_edit_subscription)).assertIsDisplayed()
    }

    private fun clickNavItem(labelRes: Int) {
        composeRule.onNode(hasText(string(labelRes)) and hasClickAction()).performClick()
        composeRule.waitForIdle()
    }

    private fun assertCurrentRoute(route: KClass<*>) {
        composeRule.runOnIdle {
            val destination = navController.currentBackStackEntry?.destination
            assertTrue(
                "Expected ${route.simpleName}, was ${destination?.route}",
                destination?.hasRoute(route) == true,
            )
        }
    }

    private fun string(resId: Int): String = context.getString(resId)
}

class SubscriptionEditRouteTest {

    @Test
    fun defaultRoute_isNewSubscription() {
        assertTrue(SubscriptionEditRoute().isNew)
    }

    @Test
    fun routeWithId_isEdit() {
        val route = SubscriptionEditRoute(subscriptionId = 5)

        assertEquals(false, route.isNew)
        assertEquals(5L, route.subscriptionId)
    }
}
