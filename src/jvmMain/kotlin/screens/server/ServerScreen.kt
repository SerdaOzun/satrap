package screens.server

import AppViewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import moe.tlaster.precompose.navigation.Navigator
import screens.server.userAndTagTab.UserTagTab
import screens.serverList.ServerViewModel
import ui.theme.spacing

/**
 * Screen for editing Server details
 */
@Composable
fun ServerScreen(
    navigator: Navigator,
    serverVm: ServerViewModel = AppViewModels.serverVM
) {
    Column(modifier = Modifier.padding(MaterialTheme.spacing.large)) {
        Text(
            "Server",
            fontSize = MaterialTheme.typography.h1.fontSize,
            fontWeight = MaterialTheme.typography.h1.fontWeight,
            modifier = Modifier.padding(bottom = MaterialTheme.spacing.medium)
        )
        Row {
            ServerTab(modifier = Modifier.weight(0.5f), navigator, serverVm)
            Spacer(modifier = Modifier.weight(0.02f).fillMaxSize())
            UserTagTab(modifier = Modifier.weight(0.5f))
        }
    }

}