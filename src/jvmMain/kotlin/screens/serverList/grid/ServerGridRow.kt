package screens.serverList.grid

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
    var selectedUser: User? by remember {
        mutableStateOf(
            if (serverComplete.users.isEmpty()) null else
                serverComplete.users.firstOrNull { it.userId == serverComplete.server.defaultUserId }
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

        //If the selectedUser is empty or its id is < 0 (meaning SSH Agent user) do not specify a username
        val sshAction = if (selectedUser == null || selectedUser?.userId!! < 0) {
            "ssh ${serverComplete.server.serverUrl}"
        } else {
            "ssh ${selectedUser?.username}@${serverComplete.server.serverUrl}"
        }

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
        val userOptions = serverComplete.users.toMutableList()
        if (serverComplete.server.showSSHAgent) {
            userOptions += User("SSH Agent")
        }
        TerminalCombobox(
            modifier = Modifier.weight(ServerHeader.USER.weight).fillMaxHeight(),
            selectedUser,
            userOptions
        ) { u -> selectedUser = u }
        //Description
        TableCell(
            text = serverComplete.server.description,
            modifier = Modifier.weight(ServerHeader.DESCRIPTION.weight)
        )
    }
}
