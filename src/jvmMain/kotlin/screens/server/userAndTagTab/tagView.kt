package screens.server.userAndTagTab

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import domain.Tag
import screens.tagManagement.TagEvent
import screens.tagManagement.TagViewModel
import ui.components.ColorRow
import ui.components.TerminalTextButton
import ui.components.TerminalTextField
import ui.components.terminalTheme
import ui.theme.LightGreen
import ui.theme.spacing

@Composable
fun TagTextfieldList(
    tagVM: TagViewModel
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val allTags by tagVM.allTags.collectAsState(emptyList())

        Row(Modifier.weight(0.85f)) {
            val state = rememberLazyListState()
            LazyColumn(modifier = Modifier.fillMaxSize().weight(0.85f), state) {
                items(allTags) { tag ->
                    tagItem(tag, tagVM)
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

        Divider(thickness = 3.dp, color = MaterialTheme.colors.onBackground)



        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)
                .padding(MaterialTheme.spacing.medium)
        ) {
            var newTagname by remember { mutableStateOf("") }

            TerminalTextField(
                label = "New Tag...",
                value = newTagname,
                onValueChange = { newTagname = it },
                modifier = Modifier.weight(0.7f).fillMaxSize().padding(end = MaterialTheme.spacing.extraSmall)
                    .border(width = 1.dp, color = MaterialTheme.colors.onPrimary)
                    .onKeyEvent { event ->
                        if (event.key == Key.Enter) {
                            if (newTagname.isNotEmpty()) {
                                tagVM.onEvent(
                                    TagEvent.InsertTag(Tag(newTagname, syncTag = true))
                                )
                                newTagname = ""
                            }
                            true
                        } else false
                    }
            )

            TerminalTextButton(modifier = Modifier.weight(0.3f).terminalTheme().fillMaxSize(), onClick = {
                if (newTagname.isNotEmpty()) {
                    tagVM.onEvent(TagEvent.InsertTag(Tag(newTagname, syncTag = true)))
                    newTagname = ""
                }
            }) {
                Text("Create Tag", color = MaterialTheme.colors.onPrimary, modifier = it)
            }
        }
    }
}


@Composable
private fun tagItem(tag: Tag, tagVM: TagViewModel) {
    val tagIsSelected = tag.tagId in tagVM.selectedTags.map { it.tagId }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(
            top = MaterialTheme.spacing.small,
            start = MaterialTheme.spacing.small,
            end = MaterialTheme.spacing.small
        ).fillMaxWidth().height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.Center
    ) {
        //Selection
        ColorRow(
            modifier = Modifier.weight(0.10f).fillMaxSize()
                .clickable {
                    tagVM.onEvent(
                        if (!tagIsSelected) TagEvent.SelectTag(tag) else TagEvent.UnselectTag(tag)
                    )
                }
                .background(if (tagIsSelected) MaterialTheme.colors.LightGreen else MaterialTheme.colors.background),
            horizontal = Arrangement.Center,
            withBorder = true
        ) {
        }

        //Tag + Selection on click
        Row(
            modifier = Modifier.weight(0.90f).fillMaxSize()
                .selectable(
                    selected = tagIsSelected,
                    onClick = {
                        tagVM.onEvent(
                            if (!tagIsSelected) TagEvent.SelectTag(tag) else TagEvent.UnselectTag(tag)
                        )
                    }
                )
                .background(MaterialTheme.colors.onBackground),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = tag.tag.trimEnd(),
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