package screens.userManagement

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import domain.User
import ui.components.DialogType
import ui.components.DialogWithConfirmAndCancel
import ui.components.TerminalTextField

@Composable
fun userManagementDialogs(
    showDialog: Boolean,
    dialogType: DialogType,
    userVm: UserViewModel,
    selectedUser: User?,
    onShowDialog: (Boolean) -> Unit
) {
    if (showDialog) {
        var newUser by remember { mutableStateOf("") }

        when (dialogType) {
            DialogType.CREATE -> {
                DialogWithConfirmAndCancel(
                    message = "New User",
                    content = {
                        TerminalTextField(label = "New username...", value = newUser, onValueChange = { newUser = it })
                    },
                    onConfirm = {
                        userVm.onEvent(UserEvent.InsertUser(User(username = newUser)))
                        newUser = ""
                        onShowDialog(false)
                    },
                    onDismiss = {
                        onShowDialog(false)
                        newUser = ""
                    }
                )
            }

            DialogType.RENAME -> {
                DialogWithConfirmAndCancel(
                    message = "Rename user",
                    content = {
                        TerminalTextField(label = "Enter the new name...", value = newUser, onValueChange = { newUser = it })
                    },
                    onConfirm = {
                        userVm.onEvent(UserEvent.InsertUser(selectedUser!!.copy(username = newUser)))
                        newUser = ""
                        onShowDialog(false)
                    },
                    onDismiss = {
                        onShowDialog(false)
                        newUser = ""
                    }
                )
            }

            DialogType.DELETE -> {
                DialogWithConfirmAndCancel(
                    message = "Are you sure you want to delete the user?",
                    confirmButtonLabel = "Delete",
                    onConfirm = {
                        userVm.onEvent(UserEvent.DeleteUser(selectedUser!!.userId!!))
                        onShowDialog(false)
                    },
                    confirmButtonColor = MaterialTheme.colors.error,
                    onDismiss = { onShowDialog(false) })
            }
        }

    }
}