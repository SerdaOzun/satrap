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

    override suspend fun insert(server: Server): Long? = queries.transactionWithResult {
        server.run {
            queries.insertServer(
                if (serverId < 0) null else serverId,
                serverUrl,
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

    override suspend fun get(id: Long): Server? {
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

    override suspend fun getLastInsertedId(): Long? {
        return queries.getLastInsertedId().executeAsOneOrNull()
    }
}