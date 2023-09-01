package environmentVars

import AppViewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import moe.tlaster.precompose.navigation.Navigator
import serverList.TableCell
import serverList.TableImgButton
import ui.theme.spacing

/**
 * Displays Environment Variables including those of the current JVM Process */
@Composable
fun EnvironmentVarsScreen(
    navigator: Navigator,
    envVarsViewModel: EnvVarsViewModel = AppViewModels.envVarVm
) {
    Column(Modifier.fillMaxSize().padding(MaterialTheme.spacing.medium)) {
        Headers(Modifier.weight(0.05f))

        val state = rememberLazyListState()
        val clipboardManager = LocalClipboardManager.current
        val density = LocalDensity.current

        Box(modifier = Modifier.weight(0.95f)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(end = MaterialTheme.spacing.medium),
                state = state
            ) {
                // Tabellenzeilen
                items(items = envVarsViewModel.envVariables.value) {
                    Row(
                        Modifier.height(IntrinsicSize.Min).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TableImgButton(painter = painterResource("copy.png"), modifier = Modifier.weight(0.1f)) {
                            clipboardManager.setText(AnnotatedString("${it.first}=${it.second}"))
                        }
                        Text(modifier = Modifier.weight(0.2f), text = it.first)
                        Text(
                            modifier = Modifier.weight(0.7f).padding(start = MaterialTheme.spacing.extraSmall),
                            text = it.second
                        )
                    }
                }
            }
            VerticalScrollbar(
                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                adapter = rememberScrollbarAdapter(
                    scrollState = state
                )
            )
        }
    }
}

@Composable
private fun Headers(modifier: Modifier) {
    Row(modifier.background(Color.Gray), verticalAlignment = Alignment.CenterVertically) {
        listOf("", "Key", "Value").forEach { header ->
            TableCell(
                text = header, modifier = Modifier.weight(
                    if (header.isEmpty()) 0.1f else {
                        if (header == "Key") 0.2f else 0.7f
                    }
                )
            )
        }
    }
}

