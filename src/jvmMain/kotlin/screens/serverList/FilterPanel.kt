package screens.serverList

import AppViewModels
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import screens.tagManagement.TagViewModel
import screens.userManagement.UserViewModel
import ui.components.CarbonMultiselectCombobox
import ui.components.TerminalTextField
import ui.theme.spacing

@Composable
fun FilterPanel(
    modifier: Modifier,
    serverVM: ServerViewModel = AppViewModels.serverVM,
    userVM: UserViewModel = AppViewModels.userVm,
    tagVM: TagViewModel = AppViewModels.tagVm
) {
    val allUsers by userVM.allUsers.collectAsState(emptyList())
    val allTags by tagVM.allTags.collectAsState(emptyList())

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TerminalTextField(
            label = "Search...",
            value = serverVM.searchText,
            onValueChange = { serverVM.searchText = it },
            modifier = Modifier.weight(0.4f).padding(end = MaterialTheme.spacing.extraSmall)
                .border(width = 1.dp, color = MaterialTheme.colors.onPrimary)
        )

        Column(modifier = Modifier.weight(0.3f).padding(end = MaterialTheme.spacing.extraSmall)) {
            CarbonMultiselectCombobox(
                modifier = Modifier,
                filterName = "User",
                userVM.filteredUsers.distinctBy { it.userId },
                allUsers.distinctBy { it.userId },
                { selectedOptions, selected -> selectedOptions.map { it.userId }.contains(selected.userId)},
                onClick = { user ->
                    if (userVM.filteredUsers.map { u -> u.userId }.contains(user.userId)) userVM.filteredUsers.removeIf { it.userId == user.userId}
                    else userVM.filteredUsers.add(user)
                }
            )
        }

        Column(modifier = Modifier.weight(0.3f).padding(end = MaterialTheme.spacing.extraSmall)) {
            CarbonMultiselectCombobox(
                modifier = Modifier.weight(0.3f),
                filterName = "Tag",
                selectedOptions = tagVM.filteredTags.distinctBy { it.tagId },
                allOptions = allTags.distinctBy { it.tagId },
                { selectedOptions, selected -> selectedOptions.map { it.tagId }.contains(selected.tagId)},
                onClick = { tag ->
                    if (tagVM.filteredTags.map { it.tagId }.contains(tag.tagId)) tagVM.filteredTags.removeIf { it.tagId == tag.tagId }
                    else tagVM.filteredTags.add(tag)
                }
            )
        }

    }
    // Combobox für Organisation
    // Suchfeld für serverurl/title
    // Checkboxen für Filter (als Grid)?
}
