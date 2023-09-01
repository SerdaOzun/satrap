package navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import moe.tlaster.precompose.navigation.Navigator
import ui.theme.spacing

@Composable
fun NavigationBar(modifier: Modifier, navigator: Navigator) {

    var selectedIndex by remember { mutableStateOf(0) }

    //Display all Screens as a Navigationbutton as defined in the Screen enum
    Column(
        modifier = modifier.fillMaxHeight()
            .width(IntrinsicSize.Max)
            .background(MaterialTheme.colors.onBackground),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Divider(color = MaterialTheme.colors.onSurface)
            Screen.entries.filter { it.navItem }.forEachIndexed { index, it ->
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .background(if (selectedIndex == index) MaterialTheme.colors.onSurface else Color.Transparent)
                        .selectable(selected = selectedIndex == index, onClick = {
                            navigator.navigate(it.name)
                            selectedIndex = index
                        })
                        .padding(MaterialTheme.spacing.small)
                ) {
                    Text(it.label, color = MaterialTheme.colors.surface, fontSize = 14.sp)
                }
            }
        }

        //Settings has its special Index -1, as it should not have a "selected" Attribute
        Row(
            modifier = Modifier.fillMaxWidth()//.onClick { navigator.navigate(Screen.SettingsScreen.name) }
                .background(if (selectedIndex == -1) MaterialTheme.colors.onSurface else Color.Transparent)
                .selectable(selected = selectedIndex == -1, onClick = {
                    navigator.navigate(Screen.SettingsScreen.name)
                    selectedIndex = -1
                })
                .padding(MaterialTheme.spacing.small)
        ) {
            Text(Screen.SettingsScreen.label, color = MaterialTheme.colors.surface, fontSize = 14.sp)
        }
    }
}