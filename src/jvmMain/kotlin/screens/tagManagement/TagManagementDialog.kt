package screens.tagManagement

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import domain.Tag
import ui.components.DialogType
import ui.components.DialogWithConfirmAndCancel
import ui.components.TerminalTextField

@Composable
fun TagManagementDialog(
    showDialog: Boolean,
    dialogType: DialogType,
    tagVm: TagViewModel,
    selectedTag: Tag?,
    onShowDialog: (Boolean) -> Unit
) {
    if (showDialog) {
        //Either a new tag or new tag name
        var newTag by remember { mutableStateOf("") }

        when (dialogType) {
            DialogType.CREATE -> {
                DialogWithConfirmAndCancel(
                    message = "New Tag",
                    content = {
                        TerminalTextField(label = "New Tag...", value = newTag, onValueChange = { newTag = it })
                    },
                    onConfirm = {
                        tagVm.onEvent(TagEvent.InsertTag(Tag(tag = newTag)))
                        newTag = ""
                        onShowDialog(false)
                    },
                    onDismiss = {
                        onShowDialog(false)
                        newTag = ""
                    }
                )
            }

            DialogType.RENAME -> {
                DialogWithConfirmAndCancel(
                    message = "Rename tag",
                    content = {
                        TerminalTextField(
                            label = "Enter the new name...",
                            value = newTag,
                            onValueChange = { newTag = it })
                    },
                    onConfirm = {
                        if(selectedTag != null) {
                            tagVm.onEvent(TagEvent.InsertTag(selectedTag.copy(tag = newTag)))
                            newTag = ""
                        }
                        onShowDialog(false)
                    },
                    onDismiss = {
                        onShowDialog(false)
                        newTag = ""
                    }
                )
            }

            DialogType.DELETE -> {
                DialogWithConfirmAndCancel(
                    message = "Are you sure you want to delete the tag?",
                    confirmButtonLabel = "Delete",
                    onConfirm = {
                        if (selectedTag != null) {
                            tagVm.onEvent(TagEvent.DeleteTag(selectedTag))
                        }
                        onShowDialog(false)
                    },
                    confirmButtonColor = MaterialTheme.colors.error,
                    onDismiss = { onShowDialog(false) })
            }
        }

    }
}
