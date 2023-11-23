package screens.tagManagement

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import domain.Tag
import ui.components.DialogType
import ui.components.TerminalTextButton
import ui.components.terminalTheme
import ui.theme.spacing

@Composable
fun TagMgmtButtonPanel(modifier: Modifier, tags: List<Tag>, selectedTagId: Long, tagVm: TagViewModel) {
    var dialogType by remember { mutableStateOf(DialogType.CREATE) }
    var showDialog by remember { mutableStateOf(false) }
    val selectedTag = tags.firstOrNull { it.tagId == selectedTagId  }


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
                .terminalTheme(isEnabled = selectedTag != null),
            onClick = {
                dialogType = DialogType.RENAME
                showDialog = true
            },
            enabled = selectedTag != null
        ) {
            Text("Rename", color = MaterialTheme.colors.onPrimary, modifier = it)
        }
        TerminalTextButton(
            modifier = Modifier.fillMaxHeight().terminalTheme(isError = true, isEnabled = selectedTag != null),
            onClick = {
                dialogType = DialogType.DELETE
                showDialog = true
            },
            enabled = selectedTag != null
        ) {
            Text("Delete", color = MaterialTheme.colors.onPrimary, modifier = it)
        }
    }

    tagManagementDialogs(
        showDialog = showDialog,
        dialogType = dialogType,
        tagVm = tagVm,
        selectedTag = selectedTag,
        onShowDialog = { showDialog = it }
    )

}
