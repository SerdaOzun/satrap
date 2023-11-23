package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.window.Dialog
import ui.theme.LightGreen
import ui.theme.spacing

enum class DialogType {
    CREATE, DELETE, RENAME
}

@Composable
fun DialogWithConfirmAndCancel(
    message: String,
    content: @Composable () -> Unit = {},
    confirmButtonLabel: String = "Confirm",
    confirmButtonColor: Color = MaterialTheme.colors.LightGreen,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            Modifier.clip(RectangleShape)
                .height(IntrinsicSize.Min)
                .width(IntrinsicSize.Min)
                .background(MaterialTheme.colors.background)
        ) {
            Column(modifier = Modifier.padding(MaterialTheme.spacing.small)) {
                Text(message, modifier = Modifier.padding(bottom = MaterialTheme.spacing.medium))

                content()

                Row(Modifier.padding(top = MaterialTheme.spacing.medium).fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    TerminalTextButton(
                        modifier = Modifier.padding(end = MaterialTheme.spacing.small).fillMaxHeight()
                            .terminalTheme(color = confirmButtonColor),
                        onClick = onConfirm
                    ) {
                        Text(confirmButtonLabel, color = MaterialTheme.colors.onPrimary, modifier = it)
                    }
                    TerminalTextButton(
                        modifier = Modifier.padding(end = MaterialTheme.spacing.small).fillMaxHeight()
                            .terminalTheme(color = Color.LightGray),
                        onClick = onDismiss,
                    ) {
                        Text("Cancel", color = MaterialTheme.colors.onPrimary, modifier = it)
                    }
                }
            }
        }
    }
}