package ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import ui.theme.LightGreen
import ui.theme.spacing

@Composable
fun Modifier.carbonTheme(isError: Boolean = false, isEnabled: Boolean = true): Modifier =
        background(
            when {
                isError && isEnabled -> MaterialTheme.colors.error
                !isEnabled -> MaterialTheme.colors.onSurface
                else -> MaterialTheme.colors.primary
            }
        ).clip(RectangleShape)


@Composable
fun Modifier.terminalTheme(isError: Boolean = false, color: Color = MaterialTheme.colors.LightGreen, isEnabled: Boolean = true): Modifier =
    background(
        when {
            isError && isEnabled -> MaterialTheme.colors.error
            !isEnabled -> Color.LightGray
            else -> color
        }
    ).clip(RectangleShape)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CarbonTextButton(
    modifier: Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    content: @Composable (Modifier) -> Unit
) {
    Row(modifier = modifier.clickable(enabled = enabled, onClick = onClick).height(50.dp).width(100.dp), verticalAlignment = Alignment.CenterVertically) {
        content(Modifier.padding(start = MaterialTheme.spacing.small))
    }
}

@Composable
fun TerminalTextButton(
    modifier: Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    content: @Composable (Modifier) -> Unit
) {
    ColorRow (modifier = modifier.clickable(enabled = enabled, onClick = onClick).height(50.dp).width(100.dp), verticalAlignment = Alignment.CenterVertically) {
        content(Modifier.padding(start = MaterialTheme.spacing.small))
    }
}
