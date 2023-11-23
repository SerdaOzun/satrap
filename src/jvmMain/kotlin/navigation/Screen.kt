package navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * All Screens in the application
 */
enum class Screen(
    val label: String,
    val icon: ImageVector,
    val group: ScreenGroup,
    val navItem: Boolean = true
) {
    ServerListScreen(
        label = "Servers",
        group = ScreenGroup.SSH,
        icon = Icons.Filled.Home
    ),
    UserManagementScreen(
        label = "User",
        group = ScreenGroup.SSH,
        icon = Icons.Filled.Person
    ),
    TagManagementScreen(
        label = "Tags",
        group = ScreenGroup.SSH,
        icon = Icons.Filled.Check
    ),
    ServerScreen(
        label = "Server",
        icon = Icons.Filled.Add,
        group = ScreenGroup.SSH,
        navItem = false
    ),
    EnvVarsScreen(
        label = "Environment Variables",
        group = ScreenGroup.ENVIRONMENT,
        icon = Icons.Filled.Build
    ),

    //    ScriptsScreen(
//        label = "Scripts",
//        icon = Icons.Filled.Info,
//    ),
    SettingsScreen(
        label = "Settings",
        icon = Icons.Filled.Settings,
        group = ScreenGroup.SETTINGS,
        navItem = false
    )
}

enum class ScreenGroup {
    SSH, ENVIRONMENT, SCRIPTS, SETTINGS
}