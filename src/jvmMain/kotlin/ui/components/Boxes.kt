package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ui.theme.LightBlue
import ui.theme.spacing

@Composable
fun ColorColumn(
    modifier: Modifier = Modifier.fillMaxWidth(),
    backgroundColor: Color = MaterialTheme.colors.LightBlue,
    headline: String = "",
    alignment: Arrangement.Vertical = Arrangement.Top,
    content: @Composable () -> Unit
) {
    Column(
        modifier.background(backgroundColor).border(width = 3.dp, color = MaterialTheme.colors.onBackground),
        verticalArrangement = alignment
    ) {
        if (headline.isNotEmpty()) {
            Text(
                headline,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.padding(MaterialTheme.spacing.small),
                fontWeight = FontWeight.Bold
            )
        }
        content()
    }
}

@Composable
fun ColorRow(
    modifier: Modifier = Modifier.fillMaxWidth(),
    headline: String = "",
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    horizontal: Arrangement.Horizontal = Arrangement.Start,
    withBorder: Boolean = true,
    content: @Composable () -> Unit
) {
    Row(
        modifier.border(width = if(withBorder) 3.dp else 0.dp, color = MaterialTheme.colors.onBackground),
        verticalAlignment = verticalAlignment,
        horizontalArrangement = horizontal
    ) {
        if (headline.isNotEmpty()) {
            Text(
                headline,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.padding(MaterialTheme.spacing.small),
                fontWeight = FontWeight.Bold
            )
        }
        content()
    }
}

@Composable
fun ColorRowContent(
    modifier: Modifier,
    backgroundColor: Color = MaterialTheme.colors.onPrimary,
    content: @Composable () -> Unit
) {
    Row(
        modifier.padding(
            start = MaterialTheme.spacing.medium,
            bottom = MaterialTheme.spacing.small,
            end = MaterialTheme.spacing.small
        )
            .background(backgroundColor)
            .padding(MaterialTheme.spacing.small)
    ) {
        content()
    }

}