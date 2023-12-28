package screens.serverList.grid

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import screens.serverList.ServerViewModel
import screens.serverList.util.ServerHeader
import ui.theme.spacing

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Headers(modifier: Modifier, serverVM: ServerViewModel) {
    Row(modifier.height(IntrinsicSize.Max)) {
        ServerHeader.entries.filterNot { it.label.isEmpty() }.forEach { currentHeader ->
            if (currentHeader != ServerHeader.SERVER) {
                Row(
                    modifier = Modifier.weight(currentHeader.headerWeight)
                        .fillMaxHeight()
                        .padding(start = MaterialTheme.spacing.extraSmall)
                        .background(MaterialTheme.colors.onPrimary), verticalAlignment = Alignment.CenterVertically
                ) {
                    TableCell(
                        text = currentHeader.label,
                        textColor = MaterialTheme.colors.background
                    )
                }

            } else {
                Row(modifier = Modifier
                    .fillMaxSize()
                    .weight(currentHeader.headerWeight)
                    .onClick { serverVM.serverNameSortingAscending = !serverVM.serverNameSortingAscending }
                    .background(MaterialTheme.colors.onPrimary),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TableCellWithIcon(
                        text = currentHeader.label,
                        textColor = MaterialTheme.colors.background,
                        serverVM.serverNameSortingAscending
                    )
                }
            }
        }
    }
}
