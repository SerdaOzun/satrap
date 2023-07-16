package environmentVars

import androidx.compose.runtime.mutableStateOf
import moe.tlaster.precompose.viewmodel.ViewModel

class EnvVarsViewModel : ViewModel() {

    val envVariables = mutableStateOf(System.getenv().toList())

}