package screens.proxy

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import domain.Proxy
import ui.components.DialogType
import ui.components.DialogWithConfirmAndCancel
import ui.components.TerminalTextField

@Composable
fun ProxyManagementDialog(
    showDialog: Boolean,
    dialogType: DialogType,
    proxyVM: ProxyViewModel,
    selectedProxy: Proxy?,
    onShowDialog: (Boolean) -> Unit
) {
    if (showDialog) {
        var newProxy by remember { mutableStateOf("") }

        when (dialogType) {
            DialogType.CREATE -> {
                DialogWithConfirmAndCancel(
                    message = "New Proxy",
                    content = {
                        TerminalTextField(label = "New proxy...", value = newProxy, onValueChange = { newProxy = it })
                    },
                    onConfirm = {
                        proxyVM.onEvent(ProxyEvent.InsertProxy(Proxy(title = newProxy)))
                        newProxy = ""
                        onShowDialog(false)
                    },
                    onDismiss = {
                        onShowDialog(false)
                        newProxy = ""
                    }
                )
            }

            DialogType.RENAME -> {
                DialogWithConfirmAndCancel(
                    message = "Rename proxy",
                    content = {
                        TerminalTextField(label = "Enter the new name...", value = newProxy, onValueChange = { newProxy = it })
                    },
                    onConfirm = {
                        if(selectedProxy != null) {
                            proxyVM.onEvent(ProxyEvent.InsertProxy(selectedProxy.copy(title = newProxy)))
                            newProxy = ""
                        }
                        onShowDialog(false)
                    },
                    onDismiss = {
                        onShowDialog(false)
                        newProxy = ""
                    }
                )
            }

            DialogType.DELETE -> {
                DialogWithConfirmAndCancel(
                    message = "Are you sure you want to delete the proxy?",
                    confirmButtonLabel = "Delete",
                    onConfirm = {
                        if(selectedProxy != null) {
                            proxyVM.onEvent(ProxyEvent.DeleteProxy(selectedProxy))
                        }
                        onShowDialog(false)
                    },
                    confirmButtonColor = MaterialTheme.colors.error,
                    onDismiss = { onShowDialog(false) })
            }
        }
    }
}