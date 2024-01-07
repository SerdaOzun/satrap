package screens.proxy

import AppViewModels
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import domain.JumpServer
import domain.Proxy
import domain.Server
import domain.User
import screens.serverList.ServerViewModel
import screens.serverList.grid.TableCell
import screens.userManagement.UserViewModel
import ui.components.TerminalCombobox
import ui.components.TerminalTextButton
import ui.components.TerminalTextField
import ui.components.terminalTheme
import ui.theme.spacing

@Composable
fun ProxyConfiguration(
    modifier: Modifier,
    proxy: Proxy?,
    proxyVM: ProxyViewModel,
    serverVM: ServerViewModel = AppViewModels.serverVM,
    userVM: UserViewModel = AppViewModels.userVm
) {
    val allServers by serverVM.servers.collectAsState(emptyList())
    val allUsers by userVM.allUsers.collectAsState(emptyList())

    Column(modifier.fillMaxSize()) {
        //Header
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = MaterialTheme.spacing.extraSmall)
                .background(MaterialTheme.colors.onPrimary),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TableCell(text = "Manage Jumpservers", textColor = MaterialTheme.colors.background)
        }

        if (proxy != null) {
            //Jumpservers + Scrollbar
            Column(Modifier.border(width = 3.dp, shape = RectangleShape, color = MaterialTheme.colors.onBackground)) {
                Box(modifier = Modifier.weight(0.9f)) {
                    val state = rememberLazyListState()

                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(end = MaterialTheme.spacing.medium),
                        state
                    ) {
                        item {
                            //Headers
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TableCell("#", modifier = Modifier.weight(0.15f), fontWeight = FontWeight.Bold)
                                TableCell("User", modifier = Modifier.weight(0.35f), fontWeight = FontWeight.Bold)
                                TableCell("Server", modifier = Modifier.weight(0.35f), fontWeight = FontWeight.Bold)
                                TableCell("Port", modifier = Modifier.weight(0.10f), fontWeight = FontWeight.Bold)
                                TableCell("", modifier = Modifier.weight(0.05f), fontWeight = FontWeight.Bold)
                            }
                        }

                        items(proxy.jumpserverList.sortedBy { it.order }) {
                            JumpServerItem(
                                jumpServer = it,
                                allServers = allServers.map { s -> s.server },
                                allUsers = allUsers,
                                proxyVM = proxyVM
                            )
                        }
                    }
                    VerticalScrollbar(
                        modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                        adapter = rememberScrollbarAdapter(
                            scrollState = state
                        ),
                        style = LocalScrollbarStyle.current.copy(
                            unhoverColor = MaterialTheme.colors.onPrimary,
                            hoverColor = MaterialTheme.colors.onPrimary
                        )
                    )
                }

                Row(modifier = Modifier.weight(0.1f).fillMaxWidth()) {
                    TerminalTextButton(
                        modifier = Modifier.width(175.dp).padding(MaterialTheme.spacing.small).terminalTheme(),
                        onClick = {
                            if (proxy.jumpserverList.lastOrNull()?.server != null) {
                                proxyVM.onEvent(
                                    ProxyEvent.InsertJumpserver(
                                        JumpServer(
                                            proxy.id, proxy.jumpserverList.lastOrNull()?.order?.plus(1) ?: 1
                                        )
                                    )
                                )
                            }
                        }) {
                        Text(
                            "New Jumpserver",
                            maxLines = 1,
                            modifier = Modifier.padding(MaterialTheme.spacing.small)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun JumpServerItem(
    jumpServer: JumpServer,
    allServers: List<Server>,
    allUsers: List<User>,
    proxyVM: ProxyViewModel,
) {
    Row(
        modifier = Modifier.height(IntrinsicSize.Min).padding(start = MaterialTheme.spacing.small),
        //.border(width = 1.dp, color = MaterialTheme.colors.onPrimary),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "${jumpServer.order}.", modifier = Modifier.weight(0.05f))
        IconButton(
            onClick = { proxyVM.onEvent(ProxyEvent.SortJumpserver(sortUpwards = true, jumpServer)) },
            modifier = Modifier.weight(0.05f)
        ) {
            Icon(Icons.Filled.KeyboardArrowUp, "")
        }
        IconButton(
            onClick = { proxyVM.onEvent(ProxyEvent.SortJumpserver(sortUpwards = false, jumpServer)) },
            modifier = Modifier.weight(0.05f)
        ) {
            Icon(Icons.Filled.KeyboardArrowDown, "")
        }
        TerminalCombobox(
            modifier = Modifier.weight(0.35f).fillMaxHeight(),
            selectedOption = jumpServer.user,
            defaultToFirstItem = false,
            options = allUsers
        ) { s -> proxyVM.onEvent(ProxyEvent.InsertJumpserver(jumpServer.copy(user = s))) }
        TerminalCombobox(
            modifier = Modifier.weight(0.35f).fillMaxHeight(),
            selectedOption = jumpServer.server,
            defaultToFirstItem = false,
            options = allServers
        ) { s -> proxyVM.onEvent(ProxyEvent.InsertJumpserver(jumpServer.copy(server = s))) }
        TerminalTextField(modifier = Modifier.weight(0.1f), value = jumpServer.port?.toString() ?: "", onValueChange = {
            if (it.trim().toCharArray().all { c -> c.isDigit() }) {
                proxyVM.onEvent(ProxyEvent.InsertJumpserver(jumpServer.copy(port = it.trim().toLong())))
            }
        })
        IconButton(
            onClick = { proxyVM.onEvent(ProxyEvent.DeleteJumpserver(jumpServer)) },
            modifier = Modifier.weight(0.05f)
        ) {
            Icon(Icons.Filled.Delete, "")
        }
    }
}
