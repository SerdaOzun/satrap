package data

import app.cash.sqldelight.coroutines.asFlow
import domain.Server
import domain.ServerDataSource
import domain.toServer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import satrapco.satrap.Database

class SqlDelightServer(
    db: Database,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ServerDataSource {

    private val queries = db.serverQueries

    override suspend fun insert(server: Server): Long? {
        return withContext(coroutineDispatcher) {
            async {
                server.run {
                    queries.transactionWithResult {
                        queries.insertServer(
                            serverId,
                            serverUrl,
                            title,
                            organization,
                            description,
                            syncServer,
                            defaultUserId
                        )
                        queries.getLastInsertedId().executeAsOneOrNull()
                    }
                }
            }
        }.await()
    }

    override suspend fun get(id: Long): Server? {
        return withContext(coroutineDispatcher) {
            async {
                queries.getServerById(id).executeAsOneOrNull()?.toServer()
            }
        }.await()
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
        return withContext(coroutineDispatcher) {
            async {
                queries.getLastInsertedId().executeAsOneOrNull()
            }
        }.await()
    }
}