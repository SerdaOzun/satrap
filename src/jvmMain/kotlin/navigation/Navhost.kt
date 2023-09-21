package navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.path
import screens.environmentVars.EnvironmentVarsScreen
import screens.server.ServerScreen
import screens.serverList.ServerListScreen
import screens.settings.SettingsScreen

/**
 * Every screen to which you can navigate needs to be added here
 */
@Composable
fun SatrapNavHost(navigator: Navigator) {
    NavHost(
        modifier = Modifier,
        navigator = navigator,
        initialRoute = Screen.ServerListScreen.name
    ) {
        scene(Screen.ServerListScreen.name) {
            ServerListScreen(navigator)
        }
        scene("${Screen.ServerScreen.name}/{id}?") { backStackEntry ->
            val id: Int? = backStackEntry.path<Int>("id")
            ServerScreen(navigator, id)
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