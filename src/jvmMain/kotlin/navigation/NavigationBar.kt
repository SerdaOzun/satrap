package navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import moe.tlaster.precompose.navigation.Navigator
import ui.components.ColorColumn
import ui.components.ColorRowContent
import ui.theme.LightBlue
import ui.theme.spacing

@Composable
fun NavigationBar(modifier: Modifier, navigator: Navigator) {

    var selectedIndex by remember { mutableStateOf(0) }

    //Display all Screens as a Navigationbutton as defined in the Screen enum
    Column(
        modifier = modifier.fillMaxHeight()
            .width(IntrinsicSize.Max)
            .padding(MaterialTheme.spacing.extraSmall)
            .background(MaterialTheme.colors.background)
    ) {

        Column(modifier = Modifier.weight(0.9f)) {
            ColorColumn(
                modifier = Modifier.fillMaxWidth().padding(bottom = MaterialTheme.spacing.extraSmall),
                backgroundColor = MaterialTheme.colors.onPrimary
            ) {
                Text(
                    text = "SATRAP",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colors.background,
                    modifier = Modifier.padding(MaterialTheme.spacing.medium)
                )
            }

            ColorColumn(modifier = Modifier.padding(bottom = MaterialTheme.spacing.extraSmall), headline = "SSH") {
                Screen.entries.filter { it.group == ScreenGroup.SSH && it.navItem }.forEachIndexed { index, it ->
                    ColorRowContent(
                        modifier = Modifier.fillMaxWidth().clickable { navigator.navigate(it.name) }
                    ) {
                        Text(it.label, color = MaterialTheme.colors.LightBlue, fontSize = 14.sp)
                    }
                }
            }

            ColorColumn(
                modifier = Modifier.padding(bottom = MaterialTheme.spacing.extraSmall),
                headline = "ENVIRONMENT"
            ) {
                Screen.entries.filter { it.group == ScreenGroup.ENVIRONMENT && it.navItem }
                    .forEachIndexed { index, it ->
                        ColorRowContent(
                            modifier = Modifier.fillMaxWidth().clickable { navigator.navigate(it.name) }
                        ) {
                            Text(it.label, color = MaterialTheme.colors.LightBlue, fontSize = 14.sp)
                        }
                    }
            }


        }

        ColorColumn(
            modifier = Modifier.height(IntrinsicSize.Min).fillMaxWidth().clickable { navigator.navigate(Screen.SettingsScreen.name)}, alignment = Arrangement.Center
        ) {
            Text(
                Screen.SettingsScreen.label,
                fontSize = 18.sp,
                modifier = Modifier.padding(MaterialTheme.spacing.medium)
            )
        }
    }
}