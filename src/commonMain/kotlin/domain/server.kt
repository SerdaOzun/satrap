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
 * @param syncServer should the server be sync with remote
 */
data class Server(
    val serverId: Long?,
    var serverUrl: String,
    var title: String,
    var organization: String?,
    var description: String,
    var syncServer: Boolean,
    var defaultUserId: Long?
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
    syncServer,
    defaultUserId
)

fun List<GetAllData>.toServersComplete(): List<ServerComplete> {
    val tagServerMap = this.groupBy { it.tag_id }.mapValues { (_, group) -> group.map { it.server_id } }
    val tags = this.filter { it.tag_id != null }.map {
        Tag(
            tagId = it.tag_id,
            serverIds = tagServerMap[it.tag_id],
            tag = it.tag!!,
            syncTag = it.syncTag!!
        )
    }.distinct()
    val userServersMap = this.groupBy { it.user_id }.mapValues { (_, group) -> group.map { it.server_id } }
    val user = this.filter { it.user_id != null }.map {
        User(
            userId = it.user_id,
            serverIds = userServersMap[it.user_id],
            username = it.username!!,
            role = it.role!!,
            defaultUser = it.defaultUser!!,
            syncUser = it.syncUser!!,
            userLevelDescription = it.userLevelDescription!!
        )
    }.distinct()

    return this.map {
        ServerComplete(
            Server(
                serverId = it.server_id,
                serverUrl = it.server_url,
                title = it.title,
                organization = it.organization,
                description = it.description,
                syncServer = it.syncServer,
                defaultUserId = it.defaultUserId
            ),
            tags.filter { tag -> tag.serverIds!!.contains(it.server_id) },
            user.filter { user -> user.serverIds!!.contains(it.server_id) }
        )
    }
}

fun List<GetServerCompleteById>.toServerComplete(): List<ServerComplete> {
    val tagServerMap = this.groupBy { it.tag_id }.mapValues { (_, group) -> group.map { it.server_id } }
    val tags = this.filter { it.tag_id != null }.map {
        Tag(
            tagId = it.tag_id,
            serverIds = tagServerMap[it.tag_id],
            tag = it.tag!!,
            syncTag = it.syncTag!!
        )
    }.distinct()
    val userServersMap = this.groupBy { it.user_id }.mapValues { (_, group) -> group.map { it.server_id } }
    val user = this.filter { it.user_id != null }.map {
        User(
            userId = it.user_id,
            serverIds = userServersMap[it.user_id],
            username = it.username!!,
            role = it.role!!,
            defaultUser = it.defaultUser!!,
            syncUser = it.syncUser!!,
            userLevelDescription = it.userLevelDescription!!
        )
    }.distinct()

    return this.map {
        ServerComplete(
            Server(
                serverId = it.server_id,
                serverUrl = it.server_url,
                title = it.title,
                organization = it.organization,
                description = it.description,
                syncServer = it.syncServer,
                defaultUserId = it.defaultUserId
            ),
            tags.filter { tag -> tag.serverIds!!.contains(it.server_id) },
            user.filter { user -> user.serverIds!!.contains(it.server_id) }
        )
    }
}