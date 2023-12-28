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
import domain.User
import screens.serverList.ServerViewModel
import screens.serverList.util.ServerEvent
import screens.userManagement.UserEvent
import screens.userManagement.UserViewModel
import ui.components.ColorRow
import ui.components.TerminalTextButton
import ui.components.TerminalTextField
import ui.components.terminalTheme
import ui.theme.LightGreen
import ui.theme.Turquoise
import ui.theme.spacing

@Composable
fun UserCreationList(serverVM: ServerViewModel, userVM: UserViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val allUsers by userVM.allUsers.collectAsState(emptyList())

        Column {
            Row(Modifier.weight(0.85f)) {
                val state = rememberLazyListState()
                LazyColumn(modifier = Modifier.fillMaxSize().weight(0.85f), state) {
                    items(allUsers) { user ->
                        userItem(user, serverVM, userVM)
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
                var newUsername by remember { mutableStateOf("") }

                TerminalTextField(
                    label = "New Username...",
                    value = newUsername.trim(),
                    onValueChange = { newUsername = it },
                    modifier = Modifier.weight(0.7f).fillMaxSize().padding(end = MaterialTheme.spacing.extraSmall)
                        .border(width = 1.dp, color = MaterialTheme.colors.onPrimary)
                        .onKeyEvent { event ->
                            if (event.key == Key.Enter) {
                                if (newUsername.isNotEmpty()) {
                                    userVM.onEvent(
                                        UserEvent.InsertUser(User(newUsername))
                                    )
                                    newUsername = ""
                                }
                                true
                            } else false
                        }
                )

                TerminalTextButton(modifier = Modifier.weight(0.3f).terminalTheme().fillMaxSize(), onClick = {
                    if (newUsername.isNotEmpty()) {
                        userVM.onEvent(UserEvent.InsertUser(User(newUsername)))
                        newUsername = ""
                    }
                }) {
                    Text("Create User", color = MaterialTheme.colors.onPrimary, modifier = it)
                }
            }
        }
    }
}

@Composable
private fun userItem(
    user: User,
    serverVM: ServerViewModel,
    userVM: UserViewModel
) {
    val userIsSelected = user.userId in userVM.selectedUsers.map { it.userId }
    val userIsDefault = serverVM.server.defaultUserId == user.userId


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
                    userVM.onEvent(
                        if (!userIsSelected) UserEvent.SelectUser(user) else UserEvent.UnselectUser(user)
                    )
                    if (userIsDefault) {
                        serverVM.onEvent(ServerEvent.UpdateDefaultUser(null))
                    }
                }
                .background(if (userIsSelected) MaterialTheme.colors.LightGreen else MaterialTheme.colors.background),
            horizontal = Arrangement.Center,
            withBorder = true
        ) {
        }

        //User name + Selection on click
        Row(
            modifier = Modifier.weight(0.55f).fillMaxSize()
                .selectable(
                    selected = userIsSelected,
                    onClick = {
                        userVM.onEvent(
                            if (!userIsSelected) UserEvent.SelectUser(user) else UserEvent.UnselectUser(user)
                        )
                        if (userIsDefault) {
                            serverVM.onEvent(ServerEvent.UpdateDefaultUser(null))
                        }
                    }
                )
                .background(MaterialTheme.colors.onBackground),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = user.username.trimEnd(),
                modifier = Modifier.padding(
                    start = MaterialTheme.spacing.extraSmall,
                    top = MaterialTheme.spacing.medium,
                    bottom = MaterialTheme.spacing.medium
                ),
                color = MaterialTheme.colors.background,
                fontWeight = FontWeight.Bold
            )
        }

        //Default
        ColorRow(
            modifier = Modifier.weight(0.35f).fillMaxSize()
                .selectable(
                    selected = userIsDefault,
                    onClick = {
                        userVM.onEvent(UserEvent.SelectUser(user))
                        serverVM.onEvent(ServerEvent.UpdateDefaultUser(user.userId))
                    }
                )
                .background(if (userIsDefault) MaterialTheme.colors.Turquoise else MaterialTheme.colors.background),
            horizontal = Arrangement.Center,
            withBorder = true
        ) {
            Text("Default User", modifier = Modifier.padding(start = MaterialTheme.spacing.extraSmall))
        }
    }

}