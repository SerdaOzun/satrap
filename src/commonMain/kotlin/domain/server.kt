package domain

import kotlinx.coroutines.flow.Flow
import satrapinsatrap.GetAllData
import satrapinsatrap.GetServerCompleteById
import satrapinsatrap.ServerEntity

/**
 * Write and Retrieve Servers to which Users can connect via ssh. For the possible data
 * @see Server for data contents and
 * @see ServerComplete for additional data bundled with the Server such as users and tags
 */
interface ServerDataSource {
    suspend fun insertServer(server: Server): Long?
    suspend fun getServerById(id: Long): Server?
    suspend fun getServerCompleteById(id: Long): ServerComplete?
    fun getAllServerComplete(): Flow<List<ServerComplete>>
    fun getAllServer(): Flow<List<Server>>
    suspend fun deleteServerById(id: Long)
    suspend fun getLastInsertedId(): Long?
}

/**
 * An SSH-Server Entry
 * @param serverId
 * @param serverUrl
 * @param title
 * @param organization
 * @param description
 * @param sync sync server with remote ssh repository
 * @param customServer is the server created by the user and not synced from a server
 */
data class Server(
    val serverId: Long?,
    var serverUrl: String,
    var title: String,
    var organization: String?,
    var description: String,
    var sync: Boolean,
    var customServer: Boolean
)

/**
 * Includes the Server with additional data
 */
data class ServerComplete(
    val server: Server,
    val tags: List<Tag>,
    val users: List<User>
)

fun ServerEntity.toServer() = Server(
    server_id,
    server_url,
    title,
    organization,
    description,
    sync,
    customServer
)

fun List<GetAllData>.toServersComplete(): List<ServerComplete> {
    val tags = this.filter { it.tag_id != null }.map { Tag(it.tag_id, it.server_id, it.tag!!, it.customTag!!) }.distinct()
    val user = this.filter { it.user_id != null }.map {
        User(
            it.user_id,
            it.server_id,
            it.username!!,
            it.role!!,
            it.customUser!!,
            it.userLevelDescription!!
        )
    }.distinct()

    return this.map {
        ServerComplete(
            Server(
                it.server_id,
                it.server_url,
                it.title,
                it.organization,
                it.description,
                it.sync,
                it.customServer
            ),
            tags.filter { tag -> tag.serverId == it.server_id },
            user.filter { user -> user.serverId == it.server_id }
        )
    }
}

fun List<GetServerCompleteById>.toServerComplete() : List<ServerComplete> {
    val tags = this.filter { it.tag_id != null }.map { Tag(it.tag_id, it.server_id, it.tag!!, it.customTag!!) }.distinct()
    val user = this.filter { it.user_id != null }.map {
        User(
            it.user_id,
            it.server_id,
            it.username!!,
            it.role!!,
            it.customUser!!,
            it.userLevelDescription!!
        )
    }.distinct()

    return this.map {
        ServerComplete(
            Server(
                it.server_id,
                it.server_url,
                it.title,
                it.organization,
                it.description,
                it.sync,
                it.customServer
            ),
            tags.filter { tag -> tag.serverId == it.server_id },
            user.filter { user -> user.serverId == it.server_id }
        )
    }
}