package settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import config
import serverList.ServerHeader
import ui.components.CarbonCombobox
import ui.components.CarbonTextButton
import ui.components.CarbonTextfield
import ui.components.carbonTheme
import ui.theme.spacing
import util.OS
import util.os

@Composable
fun SettingsScreen() {
    Column(modifier = Modifier.padding(MaterialTheme.spacing.medium)) {


        CarbonCombobox(
            modifier = Modifier.width(IntrinsicSize.Min).padding(bottom = MaterialTheme.spacing.medium),
            config.terminal.label,
            Terminal.values().filter { it.os == os }.map { it.label }
        ) { t ->
            config.changesMade = true
            config.terminal = Terminal.byLabel(t)
        }

        if (os == OS.LINUX) {
            CarbonTextfield(
                label = "Shell",
                value = config.shell,
                onValueChange = {
                    config.changesMade
                    config.shell = it
                },
                modifier = Modifier.padding(bottom = MaterialTheme.spacing.small)
            )
        }

        Row {
            CarbonTextButton(
                modifier = Modifier.padding(end = MaterialTheme.spacing.small)
                    .carbonTheme(isEnabled = config.changesMade),
                onClick = { config.onEvent(ConfigEvent.Save) },
                enabled = config.changesMade
            ) {
                Text("Save", color = MaterialTheme.colors.onPrimary, modifier = it)
            }


            CarbonTextButton(
                modifier = Modifier.carbonTheme(isError = true),
                onClick = { config.onEvent(ConfigEvent.Reset) },
            ) {
                Text("Reset to defaults", color = MaterialTheme.colors.onPrimary, modifier = it)
            }
        }
    }
}