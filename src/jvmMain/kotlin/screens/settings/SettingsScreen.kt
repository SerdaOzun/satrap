package screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import config
import ui.components.TerminalCombobox
import ui.components.TerminalTextButton
import ui.components.TerminalTextField
import ui.components.terminalTheme
import ui.theme.spacing
import util.OS
import util.currentOS

@Composable
fun SettingsScreen() {
    Column(modifier = Modifier.padding(MaterialTheme.spacing.medium)) {
        TerminalCombobox(
            modifier = Modifier.width(IntrinsicSize.Min).padding(bottom = MaterialTheme.spacing.medium),
            config.terminal.label,
            Terminal.entries.filter { it.os == currentOS }.map { it.label }
        ) { t ->
            config.changesMade = true
            config.terminal = Terminal.byLabel(t)!!
        }

        //todo most likely remove. Only allow users to choose their shell from a combobox with tested shells
        if (currentOS == OS.LINUX) {
            TerminalTextField(
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
            TerminalTextButton(
                modifier = Modifier.padding(end = MaterialTheme.spacing.small)
                    .terminalTheme(isEnabled = config.changesMade),
                onClick = { config.onEvent(ConfigEvent.Save) },
                enabled = config.changesMade
            ) {
                Text("Save", color = MaterialTheme.colors.onPrimary, modifier = it)
            }


            TerminalTextButton(
                modifier = Modifier.terminalTheme(isError = true),
                onClick = { config.onEvent(ConfigEvent.Reset) },
            ) {
                Text("Reset to defaults", color = MaterialTheme.colors.onPrimary, modifier = it)
            }
        }
    }
}