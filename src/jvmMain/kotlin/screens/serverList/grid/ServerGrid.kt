package screens.serverList.grid

import AppViewModels
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.*
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
import moe.tlaster.precompose.navigation.Navigator
import screens.serverList.ServerViewModel
import screens.tagManagement.TagViewModel
import screens.userManagement.UserViewModel
import ui.theme.DarkGreen
import ui.theme.spacing
import util.filterSearch
import util.getServerCompleteComparator

@Preview
@Composable
fun ServerGrid(
    modifier: Modifier,
    navigator: Navigator,
    serverVM: ServerViewModel = AppViewModels.serverVM,
    userVM: UserViewModel = AppViewModels.userVm,
    tagVM: TagViewModel = AppViewModels.tagVm
) {
    val servers by serverVM.servers.collectAsState(initial = emptyList())
    var selectedId by remember { mutableStateOf(-1L) }

    Column(modifier.fillMaxSize()) {
        Headers(
            Modifier.weight(0.1f)
                .padding(end = MaterialTheme.spacing.medium, bottom = MaterialTheme.spacing.extraSmall), serverVM
        )

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
                    itemsIndexed(
                        items = servers.filterSearch(serverVM, userVM, tagVM)
                            .sortedWith(getServerCompleteComparator(serverVM.serverNameSortingAscending)),
                        key = { index, s -> s.server.serverId ?: index }
                    ) { _, it ->
                        ServerItem(it, selectedId) { s ->
                            selectedId = s
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

        Divider(color = MaterialTheme.colors.onPrimary)
        //Buttons unterhalb der Tabelle
        ButtonPanel(
            Modifier.height(IntrinsicSize.Min).padding(top = MaterialTheme.spacing.small),
            navigator,
            serverVM,
            servers.firstOrNull { it.server.serverId == selectedId }
        )
    }

}

@Composable
fun RowScope.TableCell(text: String, textColor: Color = MaterialTheme.colors.onPrimary, modifier: Modifier = Modifier) {
    Text(text = text, color = textColor, modifier = modifier.padding(MaterialTheme.spacing.small))
}

@Composable
fun RowScope.TableCellWithIcon(
    text: String,
    textColor: Color = MaterialTheme.colors.onPrimary,
    ascending: Boolean,
    modifier: Modifier = Modifier
) {
    Row(modifier.padding(MaterialTheme.spacing.small)) {
        Text(text = text, color = textColor, modifier = modifier.weight(0.95f))
        Icon(
            if (ascending) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
            "",
            modifier = Modifier.weight(0.05f),
            tint = MaterialTheme.colors.background
        )
    }
}

@Composable
fun RowScope.TableIconButton(
    icon: ImageVector = Icons.Filled.PlayArrow, modifier: Modifier, onClick: () -> Unit
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(icon, "", tint = MaterialTheme.colors.DarkGreen)
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