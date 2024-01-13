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
import androidx.compose.material.icons.filled.Delete
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
fun <T> TerminalMultiselectCombobox(
    modifier: Modifier,
    filterName: String,
    selectedOptions: List<T>,
    allOptions: List<T>,
    contains: (List<T>, T) -> Boolean,
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
            if (selectedOptions.isEmpty()) "$filterName filter options" else "${selectedOptions.size} $filterName object filtered",
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
                                Checkbox(checked = contains(selectedOptions, opt), onCheckedChange = { onClick(opt) })
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

/**
 * Important: Always selects the first item of available options, if selectedOption wasn't passed
 * @param modifier
 * @param selectedOption the selected Item of the combobox
 * @param options for selection in the combobox
 * @param defaultToFirstItem if the selectedOption is null, should it default to the first item of the options list?
 * @param withClear should it be possible to clear the selection
 * @param deleteFn function to run when clearing the combobox
 * @param onClick on an item
 */
@Composable
fun <T> TerminalCombobox(
    modifier: Modifier,
    selectedOption: T?,
    options: List<T>,
    defaultToFirstItem: Boolean = true,
    withClear: Boolean = false,
    deleteFn: () -> Unit = {},
    onClick: (T) -> Unit
) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    var expanded by remember { mutableStateOf(false) }
    Row(
        modifier = modifier.clickable(onClick = { expanded = true })
            .background(MaterialTheme.colors.surface).bottomBorder(1.dp, MaterialTheme.colors.onBackground)
            .onSizeChanged { size = it },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            selectedOption?.toString() ?: if (defaultToFirstItem) options.firstOrNull()?.toString() ?: "" else "",
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
            if (withClear) {
                DropdownMenuItem(onClick = {
                    deleteFn()
                    expanded = false
                }) {
                    Row {
                        Text("", modifier = Modifier.weight(0.9f))
                        Icon(Icons.Filled.Delete, "", modifier = Modifier.weight(0.1f))
                    }
                }
            }
            options.sortedBy { it.toString() }.forEach { opt ->
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