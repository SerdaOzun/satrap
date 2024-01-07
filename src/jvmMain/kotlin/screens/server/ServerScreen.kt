package screens.server

import AppViewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import moe.tlaster.precompose.navigation.Navigator
import screens.proxy.ProxyViewModel
import screens.server.userAndTagTab.UserTagTab
import screens.serverList.ServerViewModel
import screens.tagManagement.TagViewModel
import screens.userManagement.UserViewModel

/**
 * Screen for editing Server details
 */
@Composable
fun ServerScreen(
    navigator: Navigator,
    serverVm: ServerViewModel = AppViewModels.serverVM,
    userVm: UserViewModel = AppViewModels.userVm,
    tagVm: TagViewModel = AppViewModels.tagVm,
    proxyVm: ProxyViewModel = AppViewModels.proxyVM
) {
    Column {
        Row {
            ServerView(modifier = Modifier.weight(0.5f), navigator, serverVm, userVm, tagVm, proxyVm)
            Spacer(modifier = Modifier.weight(0.02f).fillMaxSize())
            UserTagTab(modifier = Modifier.weight(0.5f))
        }
    }
}