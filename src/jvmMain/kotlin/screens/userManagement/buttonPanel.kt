package screens.userManagement

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import domain.User
import ui.components.DialogType
import ui.components.TerminalTextButton
import ui.components.terminalTheme
import ui.theme.spacing

@Composable
fun UserMgmtButtonPanel(modifier: Modifier, users: List<User>, selectedUserId: Long, userVm: UserViewModel) {
    var dialogType by remember { mutableStateOf(DialogType.CREATE) }
    var showDialog by remember { mutableStateOf(false) }
    val userIsSelected = users.map { it.userId }.contains(selectedUserId)
    val selectedUser = users.firstOrNull { it.userId == selectedUserId }


    Row(modifier) {
        TerminalTextButton(modifier = Modifier.padding(end = MaterialTheme.spacing.small).fillMaxHeight()
            .terminalTheme(),
            onClick = {
                dialogType = DialogType.CREATE
                showDialog = true
            }) {
            Text("New", color = MaterialTheme.colors.onPrimary, modifier = it)
        }
        TerminalTextButton(
            modifier = Modifier.padding(end = MaterialTheme.spacing.small).fillMaxHeight()
                .terminalTheme(isEnabled = userIsSelected),
            onClick = {
                dialogType = DialogType.RENAME
                showDialog = true
            },
            enabled = userIsSelected
        ) {
            Text("Rename", color = MaterialTheme.colors.onPrimary, modifier = it)
        }
        TerminalTextButton(
            modifier = Modifier.fillMaxHeight().terminalTheme(isError = true, isEnabled = userIsSelected),
            onClick = {
                dialogType = DialogType.DELETE
                showDialog = true
            },
            enabled = userIsSelected
        ) {
            Text("Delete", color = MaterialTheme.colors.onPrimary, modifier = it)
        }
    }

    if (selectedUser != null) {
        userManagementDialogs(
            showDialog = showDialog,
            dialogType = dialogType,
            userVm = userVm,
            selectedUser = selectedUser,
            onShowDialog = { showDialog = it }
        )
    }
}
