package screens.server

import AppViewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import moe.tlaster.precompose.navigation.Navigator
import screens.server.userAndTagTab.UserTagTab
import screens.serverList.ServerViewModel
import screens.tagManagement.TagViewModel
import screens.userManagement.UserViewModel
import ui.theme.spacing

/**
 * Screen for editing Server details
 */
@Composable
fun ServerScreen(
    navigator: Navigator,
    serverVm: ServerViewModel = AppViewModels.serverVM,
    userVm: UserViewModel = AppViewModels.userVm,
    tagVm: TagViewModel = AppViewModels.tagVm
) {
    Column(modifier = Modifier.padding(MaterialTheme.spacing.small)) {
        Row {
            ServerTab(modifier = Modifier.weight(0.5f), navigator, serverVm, userVm, tagVm)
            Spacer(modifier = Modifier.weight(0.02f).fillMaxSize())
            UserTagTab(modifier = Modifier.weight(0.5f))
        }
    }
}