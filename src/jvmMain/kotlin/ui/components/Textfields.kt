package ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import ui.theme.fontSize

@Composable
fun CarbonTextfield(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
//    onSelected: (Boolean) -> Unit,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    singleLine: Boolean = false,
    maxLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = RectangleShape
) {

    Column(modifier) {
        if(label.isNotEmpty()) {
            Text(text = label, fontSize = MaterialTheme.fontSize.small)
        }
        TextField(
            modifier = Modifier.fillMaxWidth(),
//                .border(1.dp, color = if (isSelected) MaterialTheme.colors.primary else Color.Transparent)
//                .clickable { onSelected(!isSelected) },
            value = value,
            onValueChange = onValueChange,
            shape = shape,
            enabled = enabled,
            isError = isError,
            maxLines = maxLines
        )
    }
}