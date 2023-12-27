package domain

import kotlinx.coroutines.flow.Flow
import satrapinsatrap.ServerEntity

/**
 * Write and Retrieve Servers to which Users can connect via ssh. For the possible data
 * @see Server for data contents and
 * @see ServerComplete for additional data bundled with the Server such as users and tags
 */
interface ServerDataSource {
    suspend fun insert(server: Server): Long?
    suspend fun get(id: Long): Server?
    fun getServer(): Flow<List<Server>>
    suspend fun delete(id: Long)
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