import kotlinx.coroutines.Dispatchers
import screens.environmentVars.EnvVarsViewModel
import screens.serverList.ServerViewModel

object AppViewModels {
    private val ioDispatcher = Dispatchers.IO

    val serverVM = ServerViewModel()
    val envVarVm = EnvVarsViewModel()
}