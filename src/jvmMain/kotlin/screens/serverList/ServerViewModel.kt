package screens.serverList

import AppDatabase
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import domain.Server
import domain.ServerDataSource
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

/**
 * Operations to write and read from the database for servers
 */
class ServerViewModel(
    private val serverDataSource: ServerDataSource = AppDatabase.sqlDelightServer
) : ViewModel() {

    var server by mutableStateOf(
        Server(
            serverId = null, serverUrl = "", title = "",
            organization = "", description = "", syncServer = true, defaultUserId = null
        )
    )

    val servers = serverDataSource.getAllServerComplete()

    //Filterpanel
    var searchText by mutableStateOf("")

    //If url is empty the server can not be saved
    var serverUrlInvalid by mutableStateOf(false)

    var serverNameSortingAscending by mutableStateOf(true)

    fun onEvent(event: ServerEvent): Long? {

        suspend fun initializeServer(id: Long?) {
            server = if (id != null) {
                viewModelScope.async {
                    serverDataSource.getServerCompleteById(id)!!.server
                }.await()
            } else {
                Server(
                    serverId = null, serverUrl = "", title = "",
                    organization = "", description = "", syncServer = false, defaultUserId = null
                )
            }
        }

        /**
         * Save the server to the database
         */
        suspend fun saveServer(serverToInsert: Server): Long? {
            return viewModelScope.async {
                serverDataSource.insertServer(serverToInsert)
            }.await()
        }

        fun deleteServer(serverId: Long) {
            viewModelScope.launch {
                serverDataSource.deleteServerById(serverId)
            }
        }

        fun updateDefaultuser(userId: Long?) {
            server = server.copy(defaultUserId = userId)
        }

        var insertedServerId: Long? = null

        viewModelScope.launch {
            when (event) {
                is ServerEvent.InitializeServer -> initializeServer(event.id)
                is ServerEvent.InsertServer -> insertedServerId = saveServer(event.server)
                is ServerEvent.DeleteServer -> deleteServer(event.serverId)
                is ServerEvent.UpdateDefaultUser -> updateDefaultuser(event.userId)
            }
        }

        return insertedServerId
    }

}