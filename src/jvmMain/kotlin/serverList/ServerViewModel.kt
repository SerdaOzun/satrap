package serverList

import AppDatabase
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import domain.*
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class ServerViewModel(
    private val serverDataSource: ServerDataSource = AppDatabase.sqlDelightServer,
    private val tagDataSource: TagDataSource = AppDatabase.sqlDelightTag,
    private val userDataSource: UserDataSource = AppDatabase.sqlDelightUser
) : ViewModel() {

    val servers = serverDataSource.getAllServerComplete()

    var server by mutableStateOf(
        Server(
            serverId = null, serverUrl = "", title = "",
            organization = "", description = "", sync = false, customServer = true
        )
    )

    var searchText by mutableStateOf("")
    var selectedUsers = mutableStateListOf<User>()
    var selectedTags = mutableStateListOf<Tag>()

    /**
     * Darf abgespeichert werden
     */
    var serverInvalid by mutableStateOf(false)
    var tags by mutableStateOf(emptyList<Tag>())
        private set

    var users by mutableStateOf(emptyList<User>())
        private set

    var serverNameSortingAscending by mutableStateOf(true)

    fun onEvent(event: ServerEvent) {
        when (event) {
            is ServerEvent.InitializeServer -> initializeServer(event.id)
            is ServerEvent.InsertServer -> saveServer(event.server)
            is ServerEvent.DeleteServer -> deleteServer(event.serverId)
            is ServerEvent.GetServer -> loadServerComplete(event.serverId)

            is ServerEvent.InsertUser -> insertUser(event.user, event.userIndex)
            is ServerEvent.DeleteUser -> deleteUser(event.userIndex)

            is ServerEvent.InsertTag -> insertTag(event.tag, event.tagindex)
            is ServerEvent.DeleteTag -> deleteTag(event.tagIndex)
        }
    }

    private fun initializeServer(id: Long?) {
        if (id != null) {
            loadServerComplete(id, updateServer = true)
        } else {
            server = Server(
                serverId = null, serverUrl = "", title = "",
                organization = "", description = "", sync = false, customServer = true
            )
            tags = emptyList()
            users = emptyList()
            // Mindestens 1 leeren Tag und User sicherstellen
            onEvent(ServerEvent.InsertTag(null))
            onEvent(ServerEvent.InsertUser(null))
        }
    }

    /**
     * Save Server, Users and Tags to database
     */
    private fun saveServer(serverToInsert: Server): Long? {
        var lastInsertedId: Long? = null
        viewModelScope.launch {

            lastInsertedId = serverDataSource.insertServer(serverToInsert)
            // Delete Users and Tags
            val userIds = users.map { it.userId }
            userDataSource.getUsersByServerid(lastInsertedId!!)
                .filter { u -> !userIds.contains(u.userId) }
                .forEach { userDataSource.deleteUserById(it.userId!!) }

            val tagIds = tags.map { it.tagId }
            tagDataSource.getTagsByServerId(lastInsertedId!!)
                .filter { t -> !tagIds.contains(t.tagId) }
                .forEach { tagDataSource.deleteTagById(it.tagId!!) }

            //Updated bzw. neue User und Tags in die Datenbank schreiben
            users.filterNot { it.username.isEmpty() }
                .forEach { userDataSource.insertUser(it.copy(serverId = lastInsertedId!!)) }
            tags.filterNot { it.tag.isEmpty() }
                .forEach { tagDataSource.insertTag(it.copy(serverId = lastInsertedId!!)) }
        }
        return lastInsertedId
    }

    private fun deleteServer(serverId: Long) {
        viewModelScope.launch {
            serverDataSource.deleteServerById(serverId)
        }
    }

    private fun insertTag(tag: Tag? = null, tagIndex: Int) {
        if (tag == null) {
            tags += Tag(null, server.serverId, "", true)
            return
        }

        tags = tags.mapIndexed { index, t ->
            if (index == tagIndex) tag else t
        }
    }

    private fun deleteTag(tagIndex: Int) {
        tags = tags.filterIndexed { index, _ -> index != tagIndex }
    }

    private fun insertUser(user: User? = null, userIndex: Int) {
        if (user == null) {
            users += User(null, server.serverId, "", "", true, "")
            return
        }

        users = users.mapIndexed { index, u ->
            if (index == userIndex) user else u
        }
    }

    private fun deleteUser(userIndex: Int) {
        users = users.filterIndexed { index, _ -> index != userIndex }
    }

    private fun loadServerComplete(id: Long, updateServer: Boolean = false) {
        viewModelScope.launch {
            serverDataSource.getServerCompleteById(id)?.let {
                if (updateServer) {
                    server = it.server
                }
                users = it.users
                tags = it.tags
            }
        }
    }
}