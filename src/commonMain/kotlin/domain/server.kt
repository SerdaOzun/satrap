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
    val serverId: Long,
    var proxyId: Long?,
    var serverUrl: String,
    var title: String,
    var organization: String,
    var description: String,
    var syncServer: Boolean,
    var showSSHAgent: Boolean,
    var isSSHAgentDefault: Boolean,
    var defaultUserId: Long?
) {
    constructor(
        serverId: Long,
        serverUrl: String,
        title: String,
    ) : this(serverId, null, serverUrl, title, "", "", true, true, true, null)

    constructor(
        serverUrl: String,
        title: String,
        organization: String,
        description: String,
        syncServer: Boolean,
        defaultUserId: Long?
    ) : this(-1L, null, serverUrl, title, organization, description, syncServer, true, true, defaultUserId)

    override fun toString(): String {
        return title
    }
}

/**
 * Includes the Server with additional data
 */
data class ServerComplete(
    val server: Server,
    val tags: List<Tag>,
    val users: List<User>
)

fun ServerEntity.toServer() = Server(
    serverId = server_id,
    proxyId = proxy_id,
    serverUrl = server_url,
    title = title,
    organization = organization,
    description = description,
    syncServer = syncServer,
    showSSHAgent = showSSHAgent,
    isSSHAgentDefault = isSSHAgentDefault,
    defaultUserId = defaultUserId
)