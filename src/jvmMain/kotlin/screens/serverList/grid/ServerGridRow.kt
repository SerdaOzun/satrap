package screens.serverList.grid

import AppViewModels
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerButton
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import domain.ServerComplete
import domain.User
import screens.serverList.util.ServerHeader
import ui.components.TerminalCombobox
import util.runCommand


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ServerItem(
    serverComplete: ServerComplete,
    selectedId: Long,
    onSelect: (Long) -> Unit
) {
    val userOptions = serverComplete.users.toMutableList()
    if (serverComplete.server.showSSHAgent) {
        userOptions += User("SSH Agent")
    }

    var selectedUser: User? by remember {
        mutableStateOf(
            if (serverComplete.users.isEmpty()) null else
                serverComplete.users.firstOrNull { it.userId == serverComplete.server.defaultUserId }
                    ?: userOptions.firstOrNull { it.userId < 0 }
        )
    }
    val clipboardManager = LocalClipboardManager.current

    Row(
        modifier = Modifier
            .clickable { //Left mouse button click
                if (selectedId != serverComplete.server.serverId) onSelect(serverComplete.server.serverId)
            }
            .onClick( //Right mouse button click
                enabled = true, matcher = PointerMatcher.mouse(PointerButton.Secondary),
                onClick = { clipboardManager.setText(AnnotatedString(serverComplete.server.serverUrl)) })
            .height(IntrinsicSize.Min)
            .border(width = 1.dp, color = MaterialTheme.colors.onPrimary),
        verticalAlignment = Alignment.CenterVertically
    ) {
        //Selected
        if (serverComplete.server.serverId == selectedId) {
            Divider(
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier
                    .fillMaxHeight()  //fill the max height
                    .width(25.dp)
            )
        }

        val sshAction = getSSHCommand(selectedUser, serverComplete)

        //Run Button
        TableIconButton(icon = Icons.Filled.PlayArrow, modifier = Modifier.weight(ServerHeader.RUN.weight)) {
            runCommand(sshAction)
        }
        //Copy run action to clipboard
        TableImgButton(painter = painterResource("copy.png"), modifier = Modifier.weight(ServerHeader.COPY.weight)) {
            clipboardManager.setText(AnnotatedString(sshAction))
        }
        //Servername/Serverurl
        TableCell(
            text = serverComplete.server.title.ifEmpty { serverComplete.server.serverUrl },
            modifier = Modifier.weight(ServerHeader.SERVER.weight)
        )
        //User Selection

        TerminalCombobox(
            modifier = Modifier.weight(ServerHeader.USER.weight).fillMaxHeight(),
            selectedOption = selectedUser,
            options = userOptions
        ) { u -> selectedUser = u }
        //Description
        TableCell(
            text = serverComplete.server.description,
            modifier = Modifier.weight(ServerHeader.DESCRIPTION.weight)
        )
    }
}

private fun getSSHCommand(selectedUser: User?, serverComplete: ServerComplete): String {
    val proxyVm = AppViewModels.proxyVM

    val sshCommand = StringBuilder()
    sshCommand.append("ssh ")

    serverComplete.server.proxyId?.let { proxyId ->
        val selectedProxy = proxyVm.getProxy(proxyId)

        if(!selectedProxy?.jumpserverList.isNullOrEmpty() && selectedProxy?.jumpserverList?.all { it.server == null || it.server.serverId < 0} != true ) {
            sshCommand.append("-J ")
        }
        selectedProxy?.jumpserverList?.forEachIndexed { index, js ->
            //Skip if this jumpserver has no server selected
            if (js.server == null || js.server.serverId < 0) {
                //If this was the last server add a space
                if (selectedProxy.jumpserverList.lastIndex == index && selectedProxy.jumpserverList.size != 1) {
                    sshCommand.append(" ")
                }
                return@forEachIndexed
            }

            //If there is more than one jumpserver and this is not the first item add a comma
            if(selectedProxy.jumpserverList.size > 1 && index != 0) {
                sshCommand.append(",")
            }
            //Add User
            js.user?.username?.let { username ->
                if (username.isNotEmpty()) {
                    sshCommand.append("$username@")
                }
            }
            //Add Server
            js.server.serverUrl.let { serverUrl -> sshCommand.append(serverUrl) }
            //Add Port
            js.port?.let { port ->
                if (port != 22L) {
                    sshCommand.append(":$port")
                }
            }

            //If it is the last item add a space
            if (selectedProxy.jumpserverList.lastIndex == index) {
                sshCommand.append(" ")
            }
        }
    }

    //If the selectedUser is empty or its id is < 0 (meaning SSH Agent user) do not specify a username
    if (selectedUser == null || selectedUser.userId < 0) {
        sshCommand.append(serverComplete.server.serverUrl)
    } else {
        sshCommand.append("${selectedUser.username}@${serverComplete.server.serverUrl}")
    }

    return sshCommand.toString()
}
