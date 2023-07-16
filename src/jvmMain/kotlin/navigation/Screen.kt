package navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * All Screens in the application
 */
enum class Screen(
    val label: String,
    val icon: ImageVector,
    val navItem: Boolean = true
) {
    ServerListScreen(
        label = "ServerList",
        icon = Icons.Filled.Home
    ),
    ServerScreen(
        label = "Server",
        icon = Icons.Filled.Add,
        navItem = false
    ),
    EnvVarsScreen(
        label = "Environment Variables",
        icon = Icons.Filled.Build
    ),
//    ScriptsScreen(
//        label = "Scripts",
//        icon = Icons.Filled.Info,
//    ),
    SettingsScreen(
        label = "Settings",
        icon = Icons.Filled.Settings,
        navItem = false
    )
}