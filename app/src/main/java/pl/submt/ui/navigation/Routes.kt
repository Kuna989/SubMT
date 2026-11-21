package pl.submt.ui.navigation

import kotlinx.serialization.Serializable

/*
 * Type-safe navigation routes. Arguments are passed as constructor parameters
 * and read back with `backStackEntry.toRoute<T>()` or `savedStateHandle.toRoute<T>()`
 * in a ViewModel, so they survive configuration changes and process death.
 */

// Top-level destinations (shown in the navigation bar / rail)

@Serializable
data object SubscriptionsRoute

@Serializable
data object CalendarRoute

@Serializable
data object StatisticsRoute

@Serializable
data object SettingsRoute

// Nested destinations

@Serializable
data class SubscriptionDetailsRoute(val subscriptionId: Long)

/** Add form when [subscriptionId] is [NEW_SUBSCRIPTION], edit form otherwise. */
@Serializable
data class SubscriptionEditRoute(val subscriptionId: Long = NEW_SUBSCRIPTION) {
    val isNew: Boolean get() = subscriptionId == NEW_SUBSCRIPTION

    companion object {
        /** Room ids start at 1, so 0 never points to a stored subscription. */
        const val NEW_SUBSCRIPTION = 0L
    }
}
