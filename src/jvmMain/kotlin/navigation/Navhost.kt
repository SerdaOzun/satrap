package navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import screens.environmentVars.EnvironmentVarsScreen
import screens.server.ServerScreen
import screens.serverList.ServerListScreen
import screens.settings.SettingsScreen
import screens.tagManagement.TagManagementScreen
import screens.userManagement.UserManagementScreen
import ui.theme.spacing

/**
 * Every screen to which you can navigate needs to be added here
 */
@Composable
fun SatrapNavHost(navigator: Navigator) {
    NavHost(
        modifier = Modifier.padding(MaterialTheme.spacing.extraSmall),
        navigator = navigator,
        initialRoute = Screen.ServerListScreen.name
    ) {
        scene(Screen.ServerListScreen.name) {
            ServerListScreen(navigator)
        }
        scene(Screen.UserManagementScreen.name) {
            UserManagementScreen(navigator)
        }
        scene(Screen.TagManagementScreen.name) {
            TagManagementScreen(navigator)
        }
        scene(Screen.ServerScreen.name) {
            ServerScreen(navigator)
        }
        scene(Screen.EnvVarsScreen.name) {
            EnvironmentVarsScreen(navigator)
        }
//        scene(Screen.ScriptsScreen.name) {
//            ScriptsScreen()
//        }
        scene(Screen.SettingsScreen.name) {
            SettingsScreen()
        }
    }
}