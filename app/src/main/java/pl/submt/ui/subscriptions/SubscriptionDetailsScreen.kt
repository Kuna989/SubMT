package pl.submt.ui.subscriptions

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import pl.submt.R
import pl.submt.ui.components.PlaceholderContent
import pl.submt.ui.components.SubMtTopAppBar

/** Subscription details. Content comes in J7. */
@Composable
fun SubscriptionDetailsScreen(
    @Suppress("UNUSED_PARAMETER") subscriptionId: Long,
    onNavigateUp: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            SubMtTopAppBar(
                title = stringResource(R.string.title_subscription_details),
                onNavigateUp = onNavigateUp,
                actions = {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Filled.Edit, contentDescription = stringResource(R.string.action_edit))
                    }
                },
            )
        },
    ) { innerPadding ->
        PlaceholderContent(
            icon = Icons.Outlined.Info,
            message = stringResource(R.string.placeholder_under_construction),
            modifier = Modifier.padding(innerPadding),
        )
    }
}
