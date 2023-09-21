package screens.server.userAndTagTab

import AppViewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import screens.serverList.ServerViewModel
import ui.theme.spacing

@Composable
internal fun UserTagTab(
    modifier: Modifier,
    serverVm: ServerViewModel = AppViewModels.serverVM
) {
    var userSelected by remember { mutableStateOf(true) }

    Column(modifier = modifier.fillMaxSize().background(MaterialTheme.colors.surface)) {
        Row(Modifier.height(IntrinsicSize.Min)) {
            tabElement(
                modifier = Modifier.weight(0.3f),
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

        Column(modifier = Modifier.weight(1f).fillMaxSize()) {
            if (userSelected) {
                UserCreationList(serverVm)
            } else {
                TagTextfieldList(serverVm)
            }
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun tabElement(modifier: Modifier, text: String, isSelected: Boolean, onSelect: () -> Unit) {
    Column(
        modifier = modifier
            .background(color = if (isSelected) MaterialTheme.colors.surface else MaterialTheme.colors.onSurface)
            .onClick { onSelect() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isSelected) {
            Divider(
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
            )
        }
        Text(
            text,
            modifier = Modifier.padding(MaterialTheme.spacing.small),
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}