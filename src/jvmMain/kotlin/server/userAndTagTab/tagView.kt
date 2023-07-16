package server.userAndTagTab

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import domain.Tag
import serverList.ServerEvent
import serverList.ServerViewModel
import ui.components.CarbonTextButton
import ui.components.CarbonTextfield
import ui.components.carbonTheme
import ui.theme.spacing

@Composable
fun TagTextfieldList(
    serverVM: ServerViewModel
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize().weight(0.85f)) {
            itemsIndexed(serverVM.tags) { index, tag ->
                tagItem(index, tag, serverVM)
            }
        }
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth().weight(0.15f, false).padding(MaterialTheme.spacing.medium)
        ) {
            CarbonTextButton(modifier = Modifier.carbonTheme(), onClick = {
                if (serverVM.tags.none { it.tag.isEmpty() }) {
                    serverVM.onEvent(ServerEvent.InsertTag(null, serverVM.tags.size))
                }
            }) {
                Text("Add Tags", color = MaterialTheme.colors.onPrimary, modifier = it)
            }
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun tagItem(tagIndex: Int, tag: Tag, serverVM: ServerViewModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(
            top = MaterialTheme.spacing.large,
            start = MaterialTheme.spacing.large,
            end = MaterialTheme.spacing.large
        ).fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text("${tagIndex + 1}.", modifier = Modifier.weight(0.05f))
        CarbonTextfield(
            label = "",
            value = tag.tag.trimEnd(),
            onValueChange = { serverVM.onEvent(ServerEvent.InsertTag(tag.copy(tag = it), tagIndex)) },
            //Neuen Tag erstellen bei Klick auf 'Enter'
            modifier = Modifier.weight(0.7f).onKeyEvent {
                if (it.key == Key.Enter) {
                    if (serverVM.tags.none { it.tag.isEmpty() }) {
                        serverVM.onEvent(ServerEvent.InsertTag(null, serverVM.tags.size))
                    }
                    true
                } else false
            }
        )

        IconButton(
            modifier = Modifier.weight(0.1f).padding(start = MaterialTheme.spacing.small),
            onClick = {
                if (serverVM.tags.none { it.tag.isEmpty() }) {
                    serverVM.onEvent(ServerEvent.InsertTag(null, serverVM.tags.size))
                }
            }) {
            Icon(Icons.Filled.Add, "Add")
        }
        IconButton(
            modifier = Modifier.weight(0.1f).padding(start = MaterialTheme.spacing.extraSmall),
            onClick = { serverVM.onEvent(ServerEvent.DeleteTag(tagIndex)) }) {
            Icon(Icons.Filled.Delete, "Delete")
        }
    }
}