package screens.server

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import errorMessage
import moe.tlaster.precompose.navigation.Navigator
import navigation.Screen
import screens.proxy.ProxyViewModel
import screens.serverList.ServerViewModel
import screens.serverList.util.ServerEvent
import screens.tagManagement.TagEvent
import screens.tagManagement.TagViewModel
import screens.userManagement.UserEvent
import screens.userManagement.UserViewModel
import ui.components.TerminalCombobox
import ui.components.TerminalTextButton
import ui.components.TerminalTextField
import ui.components.terminalTheme
import ui.theme.spacing

/**
 * Left Side of the Server screen concerning details of the server
 */
@Composable
internal fun ServerView(
    modifier: Modifier,
    navigator: Navigator,
    serverVm: ServerViewModel,
    userVm: UserViewModel,
    tagVm: TagViewModel,
    proxyVm: ProxyViewModel
) {

    val proxies by proxyVm.proxies.collectAsState(emptyList())
    var selectedProxy by remember { mutableStateOf(proxyVm.getProxy(serverVm.server.proxyId)) }

    LaunchedEffect(key1 = serverVm.server.proxyId) {
        //Get proxy if exists, otherwise reset to null
        selectedProxy = proxyVm.getProxy(serverVm.server.proxyId)
    }

    Column(
        modifier = modifier.fillMaxSize()
            .background(MaterialTheme.colors.surface),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.padding(
                end = MaterialTheme.spacing.medium
            ).fillMaxWidth(), horizontalAlignment = Alignment.Start

        ) {
            Row(
                Modifier.padding(bottom = MaterialTheme.spacing.small).fillMaxWidth()
                    .border(width = 3.dp, color = MaterialTheme.colors.onBackground)

            ) {
                Text(
                    "Server",
                    color = MaterialTheme.colors.onBackground,
                    modifier = Modifier.padding(MaterialTheme.spacing.small),
                    fontWeight = FontWeight.Bold
                )
            }

            //Textfields
            LabelAndField(
                modifier = Modifier.fillMaxWidth(),
                label = "URL",
                value = serverVm.server.serverUrl,
                errorHandling = true,
                maxLines = 3
            ) {
                serverVm.server = serverVm.server.copy(serverUrl = it)
            }
            LabelAndField(
                Modifier.fillMaxWidth().padding(top = MaterialTheme.spacing.small),
                "Title",
                serverVm.server.title
            ) {
                serverVm.server = serverVm.server.copy(title = it)
            }
            LabelAndField(
                Modifier.fillMaxWidth().padding(top = MaterialTheme.spacing.small),
                "Organization",
                serverVm.server.organization
            ) {
                serverVm.server = serverVm.server.copy(organization = it)
            }
            LabelAndField(
                Modifier.fillMaxWidth().padding(top = MaterialTheme.spacing.small),
                "Description",
                serverVm.server.description,
                maxLines = 5
            ) {
                serverVm.server = serverVm.server.copy(description = it)
            }
            Row(
                modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max).padding(top = MaterialTheme.spacing.small)
            ) {
                LabelComboboWithDeleteIcon(
                    modifier = Modifier.weight(1.0f).padding(end = MaterialTheme.spacing.extraSmall),
                    label = "Jump Host",
                    value = selectedProxy ,
                    options = proxies,
                    onDelete = {
                        serverVm.server = serverVm.server.copy(proxyId = null)
                    },
                    onSelect = { serverVm.server = serverVm.server.copy(proxyId = it.id) }
                )
            }
        }

        //Buttons
        SaveAndCancelButton(
            Modifier.padding(bottom = MaterialTheme.spacing.medium, start = MaterialTheme.spacing.medium),
            serverVm,
            userVm,
            tagVm,
            navigator
        )
    }
}

/**
 * Display a Label together with a Textfield in a Row
 */
@Composable
private fun LabelAndField(
    modifier: Modifier,
    label: String,
    value: String,
    errorHandling: Boolean = false,
    maxLines: Int = 1,
    isError: (Boolean) -> Unit = {},
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Min).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Row(
            modifier = Modifier.weight(0.3f).fillMaxSize().background(MaterialTheme.colors.onPrimary),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                label,
                color = MaterialTheme.colors.background,
                modifier = Modifier.padding(start = MaterialTheme.spacing.small)
            )
        }

        TerminalTextField(
            modifier = Modifier.weight(0.7f).fillMaxSize(),
            value = value,
            onValueChange = onValueChange,
            maxLines = maxLines,
            isError = if (errorHandling) {
                isError(value.isEmpty())
                value.isEmpty()
            } else false
        )
    }
}

/**
 * Display a Label together with a Textfield in a Row
 */
@Composable
private fun <T> LabelComboboWithDeleteIcon(
    modifier: Modifier,
    label: String,
    value: T?,
    options: List<T>,
    onDelete: () -> Unit,
    onSelect: (T) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Row(
            modifier = Modifier.weight(0.3f).fillMaxSize().background(MaterialTheme.colors.onPrimary),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                label,
                color = MaterialTheme.colors.background,
                modifier = Modifier.padding(start = MaterialTheme.spacing.small)
            )
        }
        TerminalCombobox(
            modifier = Modifier.weight(0.6f).fillMaxHeight(),
            selectedOption = value,
            options = options,
            defaultToFirstItem = false
        ) { selected -> onSelect(selected) }
        IconButton(modifier = Modifier.weight(0.1f), onClick = onDelete) {
            Icon(Icons.Filled.Delete, "")
        }
    }
}

@Composable
private fun SaveAndCancelButton(
    modifier: Modifier,
    serverVm: ServerViewModel,
    userVm: UserViewModel,
    tagVm: TagViewModel,
    navigator: Navigator
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Start
    ) {
        TerminalTextButton(
            onClick = {
                serverVm.serverUrlInvalid = serverVm.server.serverUrl.isEmpty()

                if (!serverVm.serverUrlInvalid) {
                    val title = serverVm.server.title.ifEmpty { serverVm.server.serverUrl }
                    val insertedServerId =
                        serverVm.onEvent(ServerEvent.InsertServer(serverVm.server.copy(title = title)))

                    if (insertedServerId != null) {
                        userVm.onEvent(UserEvent.SaveServerConfiguration(insertedServerId))
                        tagVm.onEvent(TagEvent.SaveServerConfiguration(insertedServerId))
                        navigator.navigate(Screen.ServerListScreen.name)
                    }

                    errorMessage = if (insertedServerId == null) {
                        "An error occurred. Please try again"
                    } else {
                        ""
                    }
                }
            },
            modifier = Modifier.padding(end = MaterialTheme.spacing.small).terminalTheme(),
        ) {
            Text("Save", color = MaterialTheme.colors.onPrimary, modifier = it)
        }
        TerminalTextButton(
            onClick = {
                navigator.navigate(Screen.ServerListScreen.name)
                errorMessage = ""
            },
            modifier = Modifier.terminalTheme(true)
        ) {
            Text("Cancel", color = MaterialTheme.colors.onPrimary, modifier = it)
        }

        if (serverVm.serverUrlInvalid) {
            errorMessage = "URL cannot be empty."
        }
    }
}
