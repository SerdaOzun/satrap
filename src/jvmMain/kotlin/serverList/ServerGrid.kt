package serverList

import AppViewModels
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import domain.ServerComplete
import moe.tlaster.precompose.navigation.Navigator
import navigation.Screen
import ui.components.CarbonCombobox
import ui.components.CarbonTextButton
import ui.components.carbonTheme
import ui.theme.spacing
import util.filterSearch
import util.getServerCompleteComparator
import util.runCommand

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun ServerGrid(
    modifier: Modifier,
    navigator: Navigator,
    serverVM: ServerViewModel = AppViewModels.serverVM
) {
    val servers by serverVM.servers.collectAsState(initial = emptyList())
    var selectedId by remember { mutableStateOf(-1L) }

    Column(modifier.fillMaxSize()) {
        Headers(Modifier.weight(0.1f).padding(end = MaterialTheme.spacing.medium), serverVM)

        val state = rememberLazyListState()

        Box(modifier = Modifier.weight(0.8f)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(end = MaterialTheme.spacing.medium),
                state = state
            ) {
                // Tabellenzeilen
                if (servers.isEmpty()) {
                    item {
                        Text(
                            fontSize = MaterialTheme.typography.h1.fontSize,
                            fontWeight = MaterialTheme.typography.h1.fontWeight,
                            text = "No Servers configured yet."
                        )
                    }
                } else {
                    items(
                        items = servers.filterSearch(serverVM)
                            .sortedWith(getServerCompleteComparator(serverVM.serverNameSortingAscending)),
                        key = { s -> s.server.serverId!! }
                    ) {
                        tableContent(it, selectedId) {
                            selectedId = it
                        }
                    }
                }
            }
            VerticalScrollbar(
                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                adapter = rememberScrollbarAdapter(
                    scrollState = state
                )
            )
        }

        //Buttons unterhalb der Tabelle
        ButtonPanel(
            Modifier.weight(0.1f).padding(top = MaterialTheme.spacing.small),
            navigator,
            serverVM,
            servers.firstOrNull { it.server.serverId == selectedId }
        )
    }

}

@Composable
fun ButtonPanel(
    modifier: Modifier = Modifier, navigator: Navigator, serverVM: ServerViewModel, selectedServer: ServerComplete?
) {
    Row(modifier) {
        CarbonTextButton(modifier = Modifier.padding(end = MaterialTheme.spacing.small).carbonTheme(),
            onClick = { navigator.navigate(Screen.ServerScreen.name) }) {
            Text("New", color = MaterialTheme.colors.onPrimary, modifier = it)
        }
        CarbonTextButton(
            modifier = Modifier.padding(end = MaterialTheme.spacing.small)
                .carbonTheme(isEnabled = selectedServer != null),
            onClick = { navigator.navigate("${Screen.ServerScreen.name}/${selectedServer?.server?.serverId}") },
            enabled = selectedServer != null
        ) {
            Text("Edit", color = MaterialTheme.colors.onPrimary, modifier = it)
        }
        CarbonTextButton(
            modifier = Modifier.carbonTheme(isError = true, isEnabled = selectedServer != null),
            onClick = { serverVM.onEvent(ServerEvent.DeleteServer(selectedServer?.server?.serverId!!)) },
            enabled = selectedServer != null
        ) {
            Text("Delete", color = MaterialTheme.colors.onPrimary, modifier = it)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Headers(modifier: Modifier, serverVM: ServerViewModel) {
    Row(modifier.background(Color.Gray), verticalAlignment = Alignment.CenterVertically) {
        ServerHeader.values().forEach { currentHeader ->
            if (currentHeader != ServerHeader.SERVER) {
                TableCell(text = currentHeader.label, modifier = Modifier.weight(currentHeader.weight))
            } else {
                TableCellWithIcon(
                    text = currentHeader.label,
                    serverVM.serverNameSortingAscending,
                    modifier = Modifier.weight(currentHeader.weight).onClick {
                        serverVM.serverNameSortingAscending = !serverVM.serverNameSortingAscending
                    })
            }
        }
    }
}

@Composable
private fun tableContent(
    serverComplete: ServerComplete,
    selectedId: Long,
    onSelect: (Long) -> Unit
) {
    var selectedUser by remember { mutableStateOf(serverComplete.users.first()) }
    Row(
        modifier = Modifier.selectable(selected = serverComplete.server.serverId == selectedId,
            onClick = {
                if (selectedId != serverComplete.server.serverId) onSelect(serverComplete.server.serverId!!)
            })
            .height(IntrinsicSize.Min)
            .background(if (selectedId == serverComplete.server.serverId) Color.Gray else Color.Transparent),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TableIconButton(modifier = Modifier.weight(ServerHeader.RUN.weight)) {
            runCommand("ssh ${selectedUser.username}@${serverComplete.server.serverUrl}")
        }
        TableCell(
            text = serverComplete.server.title.ifEmpty { serverComplete.server.serverUrl },
            modifier = Modifier.weight(ServerHeader.SERVER.weight)
        )
        CarbonCombobox(
            modifier = Modifier.weight(ServerHeader.USER.weight).fillMaxHeight(),
            selectedUser,
            serverComplete.users
        ) { u -> selectedUser = u }
        TableCell(
            text = serverComplete.server.description,
            modifier = Modifier.weight(ServerHeader.DESCRIPTION.weight)
        )
    }
}

@Composable
fun RowScope.TableCell(text: String, modifier: Modifier) {
    Text(text = text, modifier.padding(MaterialTheme.spacing.small))
}

@Composable
fun RowScope.TableCellWithIcon(text: String, ascending: Boolean, modifier: Modifier) {
    Row(modifier.padding(MaterialTheme.spacing.small)) {
        Text(text = text, modifier.weight(0.95f))
        Icon(
            if (ascending) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
            "",
            modifier = Modifier.weight(0.05f)
        )
    }
}

@Composable
fun RowScope.TableIconButton(
    icon: ImageVector = Icons.Filled.PlayArrow, modifier: Modifier, onClick: () -> Unit
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(icon, "")
    }
}

@Composable
fun RowScope.TableImgButton(
    painter: Painter, modifier: Modifier, onClick: () -> Unit
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Image(painter, "")
    }
}