package pl.submt.ui.subscriptions

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Subscriptions
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import pl.submt.R
import pl.submt.ui.components.PlaceholderContent
import pl.submt.ui.components.SubMtTopAppBar

/**
 * Subscription list (start screen). The list itself, monthly total,
 * search and swipe-to-delete come in J5; for now it shows the empty state.
 */
@Composable
fun SubscriptionsScreen(
    onAddSubscription: () -> Unit,
    @Suppress("UNUSED_PARAMETER") onOpenSubscription: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { SubMtTopAppBar(title = stringResource(R.string.nav_subscriptions)) },
        floatingActionButton = {
            val addLabel = stringResource(R.string.action_add_subscription)
            ExtendedFloatingActionButton(
                onClick = onAddSubscription,
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                text = { Text(addLabel) },
                // Material 3 clears the label's semantics inside the FAB, so without this
                // TalkBack would announce an unlabeled button.
                modifier = Modifier.semantics { contentDescription = addLabel },
            )
        },
    ) { innerPadding ->
        PlaceholderContent(
            icon = Icons.Outlined.Subscriptions,
            message = stringResource(R.string.subscriptions_empty),
            modifier = Modifier.padding(innerPadding),
        )
    }
}
