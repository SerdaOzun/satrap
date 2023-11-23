package screens.server.userAndTagTab

import AppViewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import screens.serverList.ServerViewModel
import screens.tagManagement.TagViewModel
import screens.userManagement.UserViewModel
import ui.theme.spacing

@Composable
internal fun UserTagTab(
    modifier: Modifier,
    serverVm: ServerViewModel = AppViewModels.serverVM,
    userVM: UserViewModel = AppViewModels.userVm,
    tagVM: TagViewModel = AppViewModels.tagVm
) {
    var userSelected by remember { mutableStateOf(true) }

    Column(modifier = modifier.fillMaxSize().background(MaterialTheme.colors.surface)) {
        Row(Modifier.height(IntrinsicSize.Min)) {
            tabElement(
                modifier = Modifier.weight(0.3f).padding(end = MaterialTheme.spacing.extraSmall),
                "Users",
                userSelected,
                onSelect = { userSelected = true })
            tabElement(
                modifier = Modifier.weight(0.3f),
                "Tags",
                !userSelected,
                onSelect = { userSelected = false })
            Box(
                modifier = Modifier.weight(0.4f).background(MaterialTheme.colors.background)
            ) {
                Text("", modifier = Modifier.padding(MaterialTheme.spacing.small))
            }
        }

        Column(modifier = Modifier.weight(1f).fillMaxSize().border(width = 3.dp, MaterialTheme.colors.onPrimary).background(MaterialTheme.colors.background)) {
            if (userSelected) {
                UserCreationList(serverVm, userVM)
            } else {
                TagTextfieldList(tagVM)
            }
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun tabElement(modifier: Modifier, text: String, isSelected: Boolean, onSelect: () -> Unit) {
    Column(
        modifier = modifier
            .border(width = 1.dp, color = MaterialTheme.colors.onPrimary)
            .clip(CutCornerShape(bottomEnd = if (isSelected) 15.dp else 0.dp))
            .background(MaterialTheme.colors.onPrimary)
            .onClick { onSelect() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text,
            color = MaterialTheme.colors.background,
            modifier = Modifier.padding(MaterialTheme.spacing.small),
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}