package domain

import androidx.compose.runtime.Immutable
import kotlinx.coroutines.flow.Flow
import satrapinsatrap.ServerEntity

/**
 * Write and Retrieve Servers to which Users can connect via ssh. For the possible data
 * @see Server for data contents and
 * @see ServerComplete for additional data bundled with the Server such as users and tags
 */
interface ServerDataSource {
    fun insert(server: Server): Long?
    fun get(id: Long): Server?
    fun getServer(): Flow<List<Server>>
    suspend fun delete(id: Long)
    fun getLastInsertedId(): Long?
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
@Immutable
data class Server(
    val serverId: Long,
    val proxyId: Long?,
    val serverUrl: String,
    val port: Long,
    val title: String,
    val organization: String,
    val description: String,
    val syncServer: Boolean,
    val showSSHAgent: Boolean,
    val isSSHAgentDefault: Boolean,
    val defaultUserId: Long?
) {
    constructor(
        serverId: Long,
        serverUrl: String,
        title: String,
        port: Long
    ) : this(serverId, null, serverUrl, port, title, "", "", true, true, true, null)

    constructor(
        serverUrl: String,
        title: String,
        organization: String,
        description: String,
        syncServer: Boolean,
        defaultUserId: Long?
    ) : this(-1L, null, serverUrl, 22L, title, organization, description, syncServer, true, true, defaultUserId)

    override fun toString(): String {
        return title
    }
}

/**
 * Includes the Server with additional data
 */
@Immutable
data class ServerComplete(
    val server: Server,
    val tags: List<Tag>,
    val users: List<User>
)

fun ServerEntity.toServer() = Server(
    serverId = server_id,
    proxyId = proxy_id,
    serverUrl = server_url,
    port = port,
    title = title,
    organization = organization,
    description = description,
    syncServer = syncServer,
    showSSHAgent = showSSHAgent,
    isSSHAgentDefault = isSSHAgentDefault,
    defaultUserId = defaultUserId
)