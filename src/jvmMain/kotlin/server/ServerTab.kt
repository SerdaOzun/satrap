package server

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import moe.tlaster.precompose.navigation.Navigator
import navigation.Screen
import serverList.ServerEvent
import serverList.ServerViewModel
import ui.components.CarbonTextButton
import ui.components.CarbonTextfield
import ui.components.carbonTheme
import ui.theme.spacing

/**
 * Left Side of the Server screen concerning details of the server
 */
@Composable
internal fun ServerTab(
    modifier: Modifier,
    navigator: Navigator,
    serverVm: ServerViewModel
) {

    Column(
        modifier = modifier.fillMaxSize()
            .background(MaterialTheme.colors.surface),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier.padding(
                top = MaterialTheme.spacing.large,
                start = MaterialTheme.spacing.large,
                end = MaterialTheme.spacing.large
            ).fillMaxWidth(), horizontalAlignment = Alignment.Start
        ) {
            //Textfields
            LabelAndField(
                Modifier.fillMaxWidth(),
                "URL",
                serverVm.server.serverUrl,
                errorHandling = true,
                isError = { serverVm.serverInvalid = it }
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
                serverVm.server.organization ?: ""
            ) {
                serverVm.server = serverVm.server.copy(organization = it)
            }
            LabelAndField(
                Modifier.fillMaxWidth().padding(top = MaterialTheme.spacing.small),
                "Description",
                serverVm.server.description
            ) {
                serverVm.server = serverVm.server.copy(description = it)
            }


            //Checkboxes
            /*
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CarbonCheckbox(
                        serverVm.server.sync,
                        onCheckedChange = { serverVm.server = serverVm.server.copy(sync = it) })
                    Text("Sync with Server")
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    CarbonCheckbox(
                        serverVm.server.customServer,
                        onCheckedChange = { serverVm.server = serverVm.server.copy(customServer = it) })
                    Text("Custom")
                }
            }
             */
        }


        //Buttons
        SaveAndCancelButton(
            Modifier.padding(bottom = MaterialTheme.spacing.medium, start = MaterialTheme.spacing.medium),
            serverVm,
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
    isError: (Boolean) -> Unit = {},
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = modifier
    ) {
        CarbonTextfield(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            label = label,
            isError = if (errorHandling) {
                isError(value.isEmpty())
                value.isEmpty()
            } else false
        )
    }
}

@Composable
private fun SaveAndCancelButton(modifier: Modifier, serverVm: ServerViewModel, navigator: Navigator) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Start
    ) {
        CarbonTextButton(
            onClick = {
                serverVm.serverInvalid = serverVm.users.isEmpty() || serverVm.users.firstOrNull()?.username!!.isEmpty()
                if (!serverVm.serverInvalid) {
                    val title = serverVm.server.title.ifEmpty { serverVm.server.serverUrl }
                    serverVm.onEvent(ServerEvent.InsertServer(serverVm.server.copy(title = title)))
                    navigator.navigate(Screen.ServerListScreen.name)
                }
            },
            modifier = Modifier.padding(end = MaterialTheme.spacing.small).carbonTheme(),
        ) {
            Text("Save", color = MaterialTheme.colors.onPrimary, modifier = it)
        }
        CarbonTextButton(
            onClick = { navigator.navigate(Screen.ServerListScreen.name) },
            modifier = Modifier.carbonTheme(true)
        ) {
            Text("Cancel", color = MaterialTheme.colors.onPrimary, modifier = it)
        }

        if (serverVm.serverInvalid) {
            Text(
                text = "URL cannot be empty and at least one user must be configured.",
                color = MaterialTheme.colors.error,
                modifier = Modifier.padding(MaterialTheme.spacing.small)
            )
        }
    }
}
