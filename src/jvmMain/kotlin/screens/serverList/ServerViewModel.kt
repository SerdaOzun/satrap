package screens.serverList

import AppDatabase
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import data.SqlDelightServerComplete
import domain.Server
import domain.ServerDataSource
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import screens.serverList.util.ServerEvent

/**
 * Operations to write and read from the database for servers
 */
class ServerViewModel(
    private val serverDataSource: ServerDataSource = AppDatabase.sqlServer,
    private val serverCompleteDatasource: SqlDelightServerComplete = AppDatabase.sqlServerComplete
) : ViewModel() {

    var server by mutableStateOf(
        Server(
            serverUrl = "", title = "",
            organization = "", description = "", syncServer = true, defaultUserId = null
        )
    )
    val servers = serverCompleteDatasource.getAll()

    //Filterpanel
    var searchText by mutableStateOf("")

    //If url is empty the server can not be saved
    var serverUrlInvalid by mutableStateOf(false)
    var serverNameSortingAscending by mutableStateOf(true)

    fun onEvent(event: ServerEvent): Long? {
        suspend fun initializeServer(id: Long?) {
            server = if (id != null) {
                serverCompleteDatasource.get(id)?.server ?: Server(
                    serverUrl = "", title = "",
                    organization = "", description = "", syncServer = false, defaultUserId = null
                )
            } else {
                Server(
                    serverUrl = "", title = "",
                    organization = "", description = "", syncServer = false, defaultUserId = null
                )
            }
        }

        var insertedServerId: Long? = null

        viewModelScope.launch {
            when (event) {
                is ServerEvent.InitializeServer -> initializeServer(event.id)
                is ServerEvent.InsertServer -> insertedServerId = serverDataSource.insert(event.server)
                is ServerEvent.DeleteServer -> serverDataSource.delete(event.serverId)
                is ServerEvent.UpdateDefaultUser -> server = server.copy(defaultUserId = event.userId, isSSHAgentDefault = false)
            }
        }

        return insertedServerId
    }

}