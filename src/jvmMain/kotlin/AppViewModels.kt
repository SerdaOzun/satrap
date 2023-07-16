import environmentVars.EnvVarsViewModel
import kotlinx.coroutines.Dispatchers
import serverList.ServerViewModel

object AppViewModels {
    private val ioDispatcher = Dispatchers.IO

    val serverVM = ServerViewModel()
    val envVarVm = EnvVarsViewModel()
}