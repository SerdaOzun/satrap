import screens.environmentVars.EnvVarsViewModel
import screens.serverList.ServerViewModel
import screens.tagManagement.TagViewModel
import screens.userManagement.UserViewModel

object AppViewModels {
    val serverVM = ServerViewModel()
    val userVm = UserViewModel()
    val tagVm = TagViewModel()
    val envVarVm = EnvVarsViewModel()
}