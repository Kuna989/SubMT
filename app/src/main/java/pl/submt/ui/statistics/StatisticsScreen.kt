package pl.submt.ui.statistics

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import pl.submt.R
import pl.submt.ui.components.PlaceholderContent
import pl.submt.ui.components.SubMtTopAppBar

/** Monthly / yearly cost statistics – task B3. */
@Composable
fun StatisticsScreen(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = { SubMtTopAppBar(title = stringResource(R.string.nav_statistics)) },
    ) { innerPadding ->
        PlaceholderContent(
            icon = Icons.Outlined.BarChart,
            message = stringResource(R.string.placeholder_under_construction),
            modifier = Modifier.padding(innerPadding),
        )
    }
}
