package serverList

import AppViewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ui.components.CarbonMultiselectCombobox
import ui.components.CarbonTextfield
import ui.theme.fontSize
import ui.theme.spacing

@Composable
fun FilterPanel(modifier: Modifier, serverVM: ServerViewModel = AppViewModels.serverVM) {
    val servers by serverVM.servers.collectAsState(initial = emptyList())

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CarbonTextfield(
            label = "Search",
            value = serverVM.searchText,
            onValueChange = { serverVM.searchText = it },
            modifier = Modifier.weight(0.4f).padding(end = MaterialTheme.spacing.extraSmall)
        )

        Column(modifier = Modifier.weight(0.3f).padding(end = MaterialTheme.spacing.extraSmall)) {
            Text(text = "User", fontSize = MaterialTheme.fontSize.small)
            CarbonMultiselectCombobox(
                modifier = Modifier,
                serverVM.selectedUsers.distinctBy { it.username },
                servers.flatMap { it.users }.distinctBy { it.username },
                onClick = {
                    if (serverVM.selectedUsers.contains(it)) serverVM.selectedUsers.remove(it)
                    else serverVM.selectedUsers.add(it)
                }
            )
        }

        Column(modifier = Modifier.weight(0.3f).padding(end = MaterialTheme.spacing.extraSmall)) {
            Text(text = "Tag", fontSize = MaterialTheme.fontSize.small)
            CarbonMultiselectCombobox(
                modifier = Modifier.weight(0.3f),
                serverVM.selectedTags,
                servers.flatMap { it.tags }.distinctBy { it.tag },
                onClick = {
                    if (serverVM.selectedTags.contains(it)) serverVM.selectedTags.remove(it)
                    else serverVM.selectedTags.add(it)
                }
            )
        }

    }
    // Combobox für Organisation
    // Suchfeld für serverurl/title
    // Checkboxen für Filter (als Grid)?
}
