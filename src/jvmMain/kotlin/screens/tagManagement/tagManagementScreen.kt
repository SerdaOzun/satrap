package screens.tagManagement

import AppViewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import domain.Tag
import moe.tlaster.precompose.navigation.Navigator
import screens.serverList.ServerViewModel
import screens.serverList.grid.TableCell
import ui.theme.spacing

@Composable
fun TagManagementScreen(
    navigator: Navigator,
    tagVm: TagViewModel = AppViewModels.tagVm,
    serverVm: ServerViewModel = AppViewModels.serverVM
) {

    val tags by tagVm.allTags.collectAsState(initial = emptyList())

    var selectedTagId by remember { mutableStateOf(-1L) }

    Column(modifier = Modifier.padding(MaterialTheme.spacing.medium).fillMaxSize()) {
        Row(modifier = Modifier.weight(0.9f)) {
            //Left side - Tag List
            LazyColumn(
                modifier = Modifier.weight(0.6f).padding(end = MaterialTheme.spacing.medium)
            ) {
                //Header
                item {
                    Row(
                        modifier = Modifier.fillMaxSize()
                            .padding(bottom = MaterialTheme.spacing.extraSmall)
                            .background(MaterialTheme.colors.onPrimary),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TableCell(text = "Tag", textColor = MaterialTheme.colors.background)
                    }
                }
                //List of users
                items(tags) { tag ->
                    TagItem(tag, selectedTagId) { selectedTagId = it ?: -1 }
                }
            }

            //Right side - Server list
            TagAssignmentGrid(Modifier.weight(0.4f), tags, selectedTagId, tagVm, serverVm)
        }
        TagMgmtButtonPanel(Modifier.weight(0.1f), tags, selectedTagId, tagVm)
    }
}

@Composable
private fun TagItem(tag: Tag, selectedId: Long, onSelect: (Long?) -> Unit) {
    Row(
        modifier = Modifier.clickable { if (selectedId != tag.tagId) onSelect(tag.tagId) }
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
            .border(width = 1.dp, color = MaterialTheme.colors.onPrimary),
        verticalAlignment = Alignment.CenterVertically
    ) {
        //Selected
        if (selectedId == tag.tagId) {
            Divider(
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier
                    .fillMaxHeight()  //fill the max height
                    .width(25.dp)
            )
        }

        TableCell(text = tag.tag, modifier = Modifier)

    }
}