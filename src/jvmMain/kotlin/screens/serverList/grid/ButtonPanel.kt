package screens.serverList.grid

import AppViewModels
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import domain.ServerComplete
import moe.tlaster.precompose.navigation.Navigator
import navigation.Screen
import screens.serverList.ServerEvent
import screens.serverList.ServerViewModel
import screens.tagManagement.TagEvent
import screens.userManagement.UserEvent
import ui.components.TerminalTextButton
import ui.components.terminalTheme
import ui.theme.spacing


@Composable
fun ButtonPanel(
    modifier: Modifier = Modifier, navigator: Navigator, serverVM: ServerViewModel, selectedServer: ServerComplete?
) {
    Row(modifier) {
        TerminalTextButton(modifier = Modifier.padding(end = MaterialTheme.spacing.small).fillMaxHeight().terminalTheme(),
            onClick = {
                serverVM.onEvent(ServerEvent.InitializeServer(null))
                AppViewModels.userVm.onEvent(UserEvent.LoadUsers(null))
                AppViewModels.tagVm.onEvent(TagEvent.LoadTags(null))
                navigator.navigate(Screen.ServerScreen.name)
            }) {
            Text("New", color = MaterialTheme.colors.onPrimary, modifier = it)
        }
        TerminalTextButton(
            modifier = Modifier.padding(end = MaterialTheme.spacing.small).fillMaxHeight().terminalTheme(isEnabled = selectedServer != null),
            onClick = {
                serverVM.onEvent(ServerEvent.InitializeServer(selectedServer!!.server.serverId))
                AppViewModels.userVm.onEvent(UserEvent.LoadUsers(selectedServer.server.serverId))
                AppViewModels.tagVm.onEvent(TagEvent.LoadTags(selectedServer.server.serverId))
                navigator.navigate(Screen.ServerScreen.name)
            },
            enabled = selectedServer != null
        ) {
            Text("Edit", color = MaterialTheme.colors.onPrimary, modifier = it)
        }
        TerminalTextButton(
            modifier = Modifier.fillMaxHeight().terminalTheme(isError = true, isEnabled = selectedServer != null),
            onClick = { serverVM.onEvent(ServerEvent.DeleteServer(selectedServer?.server?.serverId!!)) },
            enabled = selectedServer != null
        ) {
            Text("Delete", color = MaterialTheme.colors.onPrimary, modifier = it)
        }
    }
}
