package pl.submt.ui.calendar

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import pl.submt.R
import pl.submt.ui.components.PlaceholderContent
import pl.submt.ui.components.SubMtTopAppBar

/** Calendar of upcoming payments – task B4. */
@Composable
fun CalendarScreen(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = { SubMtTopAppBar(title = stringResource(R.string.nav_calendar)) },
    ) { innerPadding ->
        PlaceholderContent(
            icon = Icons.Outlined.CalendarMonth,
            message = stringResource(R.string.placeholder_under_construction),
            modifier = Modifier.padding(innerPadding),
        )
    }
}
