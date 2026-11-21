package pl.submt.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import pl.submt.ui.calendar.CalendarScreen
import pl.submt.ui.settings.SettingsScreen
import pl.submt.ui.statistics.StatisticsScreen
import pl.submt.ui.subscriptions.SubscriptionDetailsScreen
import pl.submt.ui.subscriptions.SubscriptionEditScreen
import pl.submt.ui.subscriptions.SubscriptionsScreen

@Composable
fun SubMtNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = SubscriptionsRoute,
        modifier = modifier,
    ) {
        composable<SubscriptionsRoute> {
            SubscriptionsScreen(
                onAddSubscription = { navController.navigate(SubscriptionEditRoute()) },
                onOpenSubscription = { id -> navController.navigate(SubscriptionDetailsRoute(id)) },
            )
        }

        composable<SubscriptionDetailsRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<SubscriptionDetailsRoute>()
            SubscriptionDetailsScreen(
                subscriptionId = route.subscriptionId,
                onNavigateUp = navController::navigateUp,
                onEdit = { navController.navigate(SubscriptionEditRoute(route.subscriptionId)) },
            )
        }

        composable<SubscriptionEditRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<SubscriptionEditRoute>()
            SubscriptionEditScreen(
                isNew = route.isNew,
                onNavigateUp = navController::navigateUp,
            )
        }

        composable<CalendarRoute> {
            CalendarScreen()
        }

        composable<StatisticsRoute> {
            StatisticsScreen()
        }

        composable<SettingsRoute> {
            SettingsScreen()
        }
    }
}
