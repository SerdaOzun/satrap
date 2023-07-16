package settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import config
import ui.components.CarbonTextButton
import ui.components.CarbonTextfield
import ui.components.carbonTheme
import ui.theme.spacing
import util.OS
import util.os

@Composable
fun SettingsScreen() {
    Column(modifier = Modifier.padding(MaterialTheme.spacing.medium)) {

        CarbonTextfield(
            label = "Terminal Program",
            value = config.terminal,
            onValueChange = {
                config.changesMade = true
                config.terminal = it
            },
            modifier = Modifier.padding(bottom = MaterialTheme.spacing.small)
        )

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
                modifier = Modifier.padding(end = MaterialTheme.spacing.small).carbonTheme(isEnabled = config.changesMade),
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