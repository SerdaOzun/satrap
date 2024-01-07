package screens.proxy

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import domain.Proxy
import ui.components.DialogType
import ui.components.TerminalTextButton
import ui.components.terminalTheme
import ui.theme.spacing

@Composable
fun ProxyManagementButtonPanel(
    modifier: Modifier,
    proxies: List<Proxy>,
    selectedProxyId: Long,
    proxyVM: ProxyViewModel
) {
    var dialogType by remember { mutableStateOf(DialogType.CREATE) }
    var showDialog by remember { mutableStateOf(false) }
    val selectedProxy = proxies.firstOrNull { it.id == selectedProxyId }
    val proxyIsSelected = selectedProxy != null

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
                .terminalTheme(isEnabled = proxyIsSelected),
            onClick = {
                dialogType = DialogType.RENAME
                showDialog = true
            },
            enabled = proxyIsSelected
        ) {
            Text("Rename", color = MaterialTheme.colors.onPrimary, modifier = it)
        }
        TerminalTextButton(
            modifier = Modifier.fillMaxHeight().terminalTheme(isError = true, isEnabled = proxyIsSelected),
            onClick = {
                dialogType = DialogType.DELETE
                showDialog = true
            },
            enabled = proxyIsSelected
        ) {
            Text("Delete", color = MaterialTheme.colors.onPrimary, modifier = it)
        }
    }

    ProxyManagementDialog(
        showDialog = showDialog,
        dialogType = dialogType,
        proxyVM = proxyVM,
        selectedProxy = selectedProxy,
        onShowDialog = { showDialog = it }
    )
}
