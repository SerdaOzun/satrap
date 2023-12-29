package screens.serverList.grid

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import domain.ServerComplete
import domain.User
import screens.serverList.util.ServerHeader
import ui.components.TerminalCombobox
import util.runCommand


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

    Row(
        modifier = Modifier.clickable { if (selectedId != serverComplete.server.serverId) onSelect(serverComplete.server.serverId) }
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

        //Run Button
        TableIconButton(modifier = Modifier.weight(ServerHeader.RUN.weight)) {
            //If the selectedUser is empty or its id is < 0 (meaning SSH Agent user) do not specify a username
            if (selectedUser == null || selectedUser?.userId!! < 0) {
                runCommand("ssh ${serverComplete.server.serverUrl}")
            } else {
                runCommand("ssh ${selectedUser?.username}@${serverComplete.server.serverUrl}")
            }
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
