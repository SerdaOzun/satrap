package data

import app.cash.sqldelight.coroutines.asFlow
import domain.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import satrapin.satrap.Database

class SqlDelightServer(db: Database) : ServerDataSource {

    private val queries = db.serverQueries

    override suspend fun insertServer(server: Server): Long? {
        server.run {
            return queries.transactionWithResult {
                queries.insertServer(serverId, serverUrl, title, organization, description, sync, customServer)
                queries.getLastInsertedId().executeAsOneOrNull()
            }
        }
    }

    override suspend fun getServerById(id: Long): Server? {
        return queries.getServerById(id).executeAsOneOrNull()?.toServer()
    }

    override suspend fun getServerCompleteById(id: Long): ServerComplete? {
        return queries.getServerCompleteById(id).executeAsList().toServerComplete().firstOrNull()
    }

    override fun getAllServerComplete(): Flow<List<ServerComplete>> {
        return queries.getAllData().asFlow().map { query ->
            query.executeAsList().toServersComplete().distinct()
        }
    }

    override fun getAllServer(): Flow<List<Server>> {
        return queries.getAllServers().asFlow().map { query -> query.executeAsList().map { it.toServer() } }
    }

    override suspend fun deleteServerById(id: Long) {
        queries.deleteServerById(id)
    }

    override suspend fun getLastInsertedId(): Long? {
        return queries.getLastInsertedId().executeAsOneOrNull()
    }
}