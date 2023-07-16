package ui.components

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import ui.theme.spacing

@Composable
fun <T> CarbonMultiselectCombobox(
    modifier: Modifier,
    selectedOptions: List<T>,
    allOptions: List<T>,
    onClick: (T) -> Unit
) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    var expanded by remember { mutableStateOf(false) }
    Row(
        modifier = modifier.fillMaxHeight().clickable(onClick = { expanded = true })
            .background(MaterialTheme.colors.surface).bottomBorder(1.dp, MaterialTheme.colors.onBackground)
            .onSizeChanged { size = it },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            if (selectedOptions.isEmpty()) "Choose filter options" else "${selectedOptions.size} objects filtered",
            modifier = Modifier.weight(0.9f).padding(start = 10.dp)
        )
        Icon(
            if (expanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
            "",
            modifier = Modifier.weight(0.1f)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(size.width.dp)
        ) {
            Box(modifier = Modifier.size(size.width.dp, 300.dp)) {
                val state = rememberLazyListState()

                LazyColumn(modifier = Modifier.fillMaxSize().padding(end = MaterialTheme.spacing.medium), state) {
                    items(allOptions) { opt ->
                        DropdownMenuItem(onClick = {
                            onClick(opt)
                        }) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(checked = selectedOptions.contains(opt), onCheckedChange = {})
                                Text(text = opt.toString(), color = MaterialTheme.colors.onBackground)
                            }
                        }
                    }
                }
                VerticalScrollbar(
                    modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                    adapter = rememberScrollbarAdapter(
                        scrollState = state
                    )
                )
            }
        }
    }
}

@Composable
fun <T> CarbonCombobox(modifier: Modifier, selectedOption: T, options: List<T>, onClick: (T) -> Unit) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    var expanded by remember { mutableStateOf(false) }
    Row(
        modifier = modifier.clickable(onClick = { expanded = true })
            .background(MaterialTheme.colors.surface).bottomBorder(1.dp, MaterialTheme.colors.onBackground)
            .onSizeChanged { size = it },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(selectedOption.toString(), modifier = Modifier.weight(0.9f).padding(start = 10.dp))
        Icon(
            if (expanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
            "",
            modifier = Modifier.weight(0.1f)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(size.width.dp)
        ) {
            options.forEach { opt ->
                DropdownMenuItem(onClick = {
                    onClick(opt)
                    expanded = false
                }) {
                    Text(text = opt.toString(), color = MaterialTheme.colors.onBackground)
                }
            }
        }
    }
}