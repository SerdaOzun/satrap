package screens.serverList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import moe.tlaster.precompose.navigation.Navigator
import ui.theme.spacing

/**
 * List of Servers.
 * Add, Edit or Delete Servers. Or connect to them
 */
@Composable
fun ServerListScreen(navigator: Navigator) {
    Column(modifier = Modifier.padding(MaterialTheme.spacing.medium)) {
        FilterPanel(Modifier.height(IntrinsicSize.Min).padding(bottom = MaterialTheme.spacing.small))
        ServerGrid(Modifier,navigator)
    }
}