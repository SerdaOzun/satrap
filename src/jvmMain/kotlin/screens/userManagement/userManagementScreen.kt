package screens.userManagement

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
import domain.User
import moe.tlaster.precompose.navigation.Navigator
import screens.serverList.ServerViewModel
import screens.serverList.grid.TableCell
import ui.theme.spacing

@Composable
fun UserManagementScreen(
    navigator: Navigator,
    userVm: UserViewModel = AppViewModels.userVm,
    serverVm: ServerViewModel = AppViewModels.serverVM
) {

    val users by userVm.allUsers.collectAsState(initial = emptyList())
    val sortedUsers = remember(users) { users.sortedBy { it.username.lowercase() } }

    var selectedUserId by remember { mutableStateOf(-1L) }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.weight(0.9f)) {
            //Left side - User List
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
                        TableCell(text = "Username", textColor = MaterialTheme.colors.background)
                    }
                }
                //List of users
                items(sortedUsers) { user ->
                    UserItem(user, selectedUserId) { selectedUserId = it }
                }
            }

            //Right side - Server list
            ServerAssignmentGrid(Modifier.weight(0.4f), users, selectedUserId, userVm, serverVm)
        }
        UserManagementButtonPanel(Modifier.height(IntrinsicSize.Min).fillMaxWidth(), users, selectedUserId, userVm)
    }
}

@Composable
private fun UserItem(user: User, selectedId: Long, onSelect: (Long) -> Unit) {
    Row(
        modifier = Modifier.clickable { if (selectedId != user.userId) onSelect(user.userId) }
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
            .border(width = 1.dp, color = MaterialTheme.colors.onPrimary),
        verticalAlignment = Alignment.CenterVertically
    ) {
        //Selected
        if (selectedId == user.userId) {
            Divider(
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier
                    .fillMaxHeight()  //fill the max height
                    .width(25.dp)
            )
        }

        TableCell(text = user.username, modifier = Modifier)

    }
}