package server

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import moe.tlaster.precompose.navigation.Navigator
import server.userAndTagTab.UserTagTab
import ui.theme.spacing

@Composable
fun ServerScreen(
    navigator: Navigator,
    serverId: Int?
) {

    Column(modifier = Modifier.padding(MaterialTheme.spacing.large)) {
        Text(
            "Server",
            fontSize = MaterialTheme.typography.h1.fontSize,
            fontWeight = MaterialTheme.typography.h1.fontWeight,
            modifier = Modifier.padding(bottom = MaterialTheme.spacing.medium)
        )
        Row {
            ServerTab(modifier = Modifier.weight(0.5f), navigator, serverId)
            Spacer(modifier = Modifier.weight(0.02f).fillMaxSize())
            UserTagTab(modifier = Modifier.weight(0.5f))
        }
    }

}