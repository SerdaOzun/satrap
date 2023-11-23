import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import io.github.oshai.kotlinlogging.KotlinLogging
import moe.tlaster.precompose.PreComposeWindow
import moe.tlaster.precompose.navigation.rememberNavigator
import navigation.NavigationBar
import navigation.SatrapNavHost
import screens.settings.Config
import ui.theme.AppTheme
import ui.theme.TerminalTheme
import ui.theme.spacing
import util.currentOS
import java.awt.Dimension


fun main() {
    val logger = KotlinLogging.logger {}
    logger.info { "Running on OS: $currentOS" }

    application {
        PreComposeWindow(
            state = WindowState(size = DpSize(1200.dp, 800.dp)), title = "Satrap",
            onCloseRequest = {
                exitApplication()
            },
            icon = painterResource("satrap_logo.png")
        ) {
            window.minimumSize = Dimension(1200, 800)
            TerminalTheme(AppTheme.LIGHT) {
                App()
            }
        }
    }
}

val config by lazy { Config() }

var errorMessage by mutableStateOf("")

@Composable
fun App() {
    val navigator = rememberNavigator()

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {
        Row(modifier = Modifier.weight(0.95f)) {
            NavigationBar(Modifier.width(IntrinsicSize.Min), navigator)
            SatrapNavHost(navigator = navigator)
        }
        //Row at bottom for errors
        Row(modifier = Modifier.weight(0.05f).fillMaxWidth().background(MaterialTheme.colors.background)) {
            Text(
                errorMessage,
                modifier = Modifier.padding(MaterialTheme.spacing.small),
                fontSize = 18.sp,
                color = MaterialTheme.colors.error
            )
        }
    }
}
