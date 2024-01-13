package screens.proxy

import AppViewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import domain.Proxy
import screens.serverList.grid.TableCell
import ui.theme.spacing

@Composable
fun ProxyScreen(
    proxyVM: ProxyViewModel = AppViewModels.proxyVM
) {

    val proxies by proxyVM.proxies.collectAsState(initial = emptyList())
    var selectedProxyId by remember { mutableStateOf(-1L) }

    LaunchedEffect(Unit) {
        selectedProxyId = proxies.firstOrNull()?.id ?: -1L
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.weight(0.9f)) {
            //Left side - proxy name list
            LazyColumn(
                modifier = Modifier.weight(0.35f).padding(end = MaterialTheme.spacing.medium)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxSize()
                            .padding(bottom = MaterialTheme.spacing.extraSmall)
                            .background(MaterialTheme.colors.onPrimary),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TableCell(text = "Proxy", textColor = MaterialTheme.colors.background)
                    }
                }
                //List of proxies
                items(proxies) { proxy ->
                    ProxyItem(proxy, selectedProxyId) { selectedProxyId = it }
                }
            }

            //Right side - Proxy configuration
            JumpserverConfiguration(
                Modifier.weight(0.65f),
                proxies.firstOrNull { it.id == selectedProxyId },
                proxyVM
            )
        }
        ProxyManagementButtonPanel(Modifier.height(IntrinsicSize.Min).fillMaxWidth(), proxies, selectedProxyId, proxyVM)
    }

}

@Composable
private fun ProxyItem(proxy: Proxy, selectedId: Long, onSelect: (Long) -> Unit) {
    Row(
        modifier = Modifier.clickable { if (selectedId != proxy.id) onSelect(proxy.id) }
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
            .border(width = 1.dp, color = MaterialTheme.colors.onPrimary),
        verticalAlignment = Alignment.CenterVertically
    ) {
        //Selected
        if (selectedId == proxy.id) {
            Divider(
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier
                    .fillMaxHeight()  //fill the max height
                    .width(25.dp)
            )
        }

        TableCell(text = proxy.title, modifier = Modifier)
    }
}