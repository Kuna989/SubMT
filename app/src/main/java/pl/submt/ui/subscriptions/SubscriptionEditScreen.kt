package pl.submt.ui.subscriptions

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import pl.submt.R
import pl.submt.ui.components.PlaceholderContent
import pl.submt.ui.components.SubMtTopAppBar

/**
 * Add / edit form. Fields and validation come in J6; the ViewModel will read the
 * subscription id from `SavedStateHandle.toRoute<SubscriptionEditRoute>()`.
 */
@Composable
fun SubscriptionEditScreen(
    isNew: Boolean,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val title = stringResource(
        if (isNew) R.string.title_add_subscription else R.string.title_edit_subscription,
    )
    Scaffold(
        modifier = modifier,
        topBar = { SubMtTopAppBar(title = title, onNavigateUp = onNavigateUp) },
    ) { innerPadding ->
        PlaceholderContent(
            icon = Icons.Outlined.EditNote,
            message = stringResource(R.string.placeholder_under_construction),
            modifier = Modifier.padding(innerPadding),
        )
    }
}
