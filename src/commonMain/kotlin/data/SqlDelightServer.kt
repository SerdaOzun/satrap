package data

import app.cash.sqldelight.coroutines.asFlow
import domain.Server
import domain.ServerDataSource
import domain.toServer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import satrapco.satrap.Database

class SqlDelightServer(
    db: Database,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ServerDataSource {

    private val queries = db.serverQueries

    override fun insert(server: Server): Long? = server.run {
        if (serverId >= 0) {
            update(server)
            serverId
        } else {
            queries.transactionWithResult {
                queries.insertServer(
                    null,
                    proxyId,
                    serverUrl,
                    port,
                    title,
                    organization,
                    description,
                    syncServer,
                    showSSHAgent,
                    isSSHAgentDefault,
                    defaultUserId
                )
                queries.getLastInsertedId().executeAsOneOrNull()
            }
        }
    }

    private fun update(server: Server) {
        server.run {
            queries.updateServer(
                proxyId,
                serverUrl,
                port,
                title,
                organization,
                description,
                syncServer,
                showSSHAgent,
                isSSHAgentDefault,
                defaultUserId,
                serverId,
            )
        }
    }

    override fun get(id: Long): Server? {
        return queries.getServerById(id).executeAsOneOrNull()?.toServer()
    }

    override fun getServer(): Flow<List<Server>> {
        return queries.getAllServers().asFlow().map { query -> query.executeAsList().map { it.toServer() } }
    }

    override suspend fun delete(id: Long) {
        withContext(coroutineDispatcher) {
            queries.deleteServerById(id)
        }
    }

    override fun getLastInsertedId(): Long? {
        return queries.getLastInsertedId().executeAsOneOrNull()
    }
}