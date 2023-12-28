package screens.tagManagement

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import domain.Server
import domain.Tag
import screens.serverList.ServerViewModel
import screens.serverList.grid.TableCell
import ui.components.ColorRow
import ui.theme.LightGreen
import ui.theme.spacing

/**
 * List of Servers which can be assigned to the selected tag
 */
@Composable
private fun TagSelection(tag: Tag, tagVm: TagViewModel, serverVm: ServerViewModel) {

    /**
     * @param server
     * @param hasSelectedTagAssigned the selected tag is already assigned to the server
     * @param onSelect
     */
    @Composable
    fun ServerItem(
        server: Server,
        hasSelectedTagAssigned: Boolean,
        onSelect: () -> Unit
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.height(IntrinsicSize.Min).padding(
                start = MaterialTheme.spacing.small,
                end = MaterialTheme.spacing.small,
                bottom = MaterialTheme.spacing.small
            ).fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            //Selection
            ColorRow(
                modifier = Modifier.weight(0.10f).fillMaxSize()
                    .clickable { onSelect() }
                    .background(if (hasSelectedTagAssigned) MaterialTheme.colors.LightGreen else MaterialTheme.colors.background),
                horizontal = Arrangement.Center,
                withBorder = true
            ) {
            }

            //Server name + Selection on click
            Row(
                modifier = Modifier.weight(0.55f).fillMaxSize()
                    .selectable(
                        selected = hasSelectedTagAssigned,
                        onClick = { onSelect() }
                    )
                    .background(MaterialTheme.colors.onBackground),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = server.title,
                    modifier = Modifier.padding(
                        start = MaterialTheme.spacing.extraSmall,
                        top = MaterialTheme.spacing.medium,
                        bottom = MaterialTheme.spacing.medium
                    ),
                    color = MaterialTheme.colors.background,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    val servers = serverVm.servers.collectAsState(initial = emptyList())

    servers.value.forEach { server ->
        val serverHasTag = server.tags.map { u -> u.tagId }.contains(tag.tagId)
        ServerItem(server.server, serverHasTag) {
            if (serverHasTag) {
                tagVm.onEvent(TagEvent.DeleteTagFromServer(tag, server.server))
            } else {
                tagVm.onEvent(TagEvent.AddTagToServer(tag, server.server))
            }
        }
    }
}

@Composable
fun TagAssignmentGrid(
    modifier: Modifier,
    tags: List<Tag>,
    selectedTagId: Long,
    tagVm: TagViewModel,
    serverVm: ServerViewModel
) {
    Column(modifier.padding(bottom = MaterialTheme.spacing.extraSmall)) {
        //Header
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = MaterialTheme.spacing.extraSmall)
                .background(MaterialTheme.colors.onPrimary),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TableCell(text = "Add Tag to Servers", textColor = MaterialTheme.colors.background)
        }

        //Server List + Scrollbar
        Row(Modifier.border(width = 3.dp, shape = RectangleShape, color = MaterialTheme.colors.onBackground)) {
            val state = rememberLazyListState()

            LazyColumn(
                modifier = Modifier.fillMaxSize().weight(0.85f).padding(top = MaterialTheme.spacing.small),
                state
            ) {
                if (tags.map { it.tagId }.contains(selectedTagId)) {
                    item {
                        TagSelection(tags.first { it.tagId == selectedTagId }, tagVm, serverVm)
                    }
                }
            }
            VerticalScrollbar(
                modifier = Modifier.fillMaxHeight().width(10.dp),
                adapter = rememberScrollbarAdapter(
                    scrollState = state
                ),
                style = LocalScrollbarStyle.current.copy(
                    unhoverColor = MaterialTheme.colors.onPrimary,
                    hoverColor = MaterialTheme.colors.onPrimary
                )
            )
        }
    }
}
