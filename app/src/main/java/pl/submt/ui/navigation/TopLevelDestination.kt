package pl.submt.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Subscriptions
import androidx.compose.ui.graphics.vector.ImageVector
import pl.submt.R
import kotlin.reflect.KClass

/** Destinations reachable from the navigation bar (phones) or rail (tablets, landscape). */
enum class TopLevelDestination(
    val route: Any,
    val routeClass: KClass<*>,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val labelRes: Int,
) {
    SUBSCRIPTIONS(
        route = SubscriptionsRoute,
        routeClass = SubscriptionsRoute::class,
        selectedIcon = Icons.Filled.Subscriptions,
        unselectedIcon = Icons.Outlined.Subscriptions,
        labelRes = R.string.nav_subscriptions,
    ),
    CALENDAR(
        route = CalendarRoute,
        routeClass = CalendarRoute::class,
        selectedIcon = Icons.Filled.CalendarMonth,
        unselectedIcon = Icons.Outlined.CalendarMonth,
        labelRes = R.string.nav_calendar,
    ),
    STATISTICS(
        route = StatisticsRoute,
        routeClass = StatisticsRoute::class,
        selectedIcon = Icons.Filled.BarChart,
        unselectedIcon = Icons.Outlined.BarChart,
        labelRes = R.string.nav_statistics,
    ),
    SETTINGS(
        route = SettingsRoute,
        routeClass = SettingsRoute::class,
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
        labelRes = R.string.nav_settings,
    ),
}
