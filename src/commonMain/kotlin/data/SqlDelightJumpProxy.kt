package data

import app.cash.sqldelight.coroutines.asFlow
import domain.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import satrapco.satrap.Database
import satrapinsatrap.GetAllProxyComplete

class SqlDelightJumpProxy(
    db: Database,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val proxyQueries = db.proxyQueries
    private val jumpserverQueries = db.jumpserverQueries

    fun insertProxy(proxy: Proxy): Long? {
        proxyQueries.insert(
            if (proxy.id < 0) null else proxy.id,
            proxy.title
        )
        return proxyQueries.getLastInsertedId().executeAsOneOrNull()
    }

    fun insertJumpserver(jumpserver: JumpServer): Long? = jumpserver.run {
        if (id >= 0) {
            updateJumpserver(this)
            id
        } else {
            jumpserverQueries.transactionWithResult {
                jumpserverQueries.insert(
                    null,
                    proxy_id = proxyId,
                    jumpserver_order = order,
                    jumpserver_userId = user?.userId,
                    jumpsserver_serverId = server?.serverId,
                    jumpserver_port = port
                )
                jumpserverQueries.getLastInsertedId().executeAsOneOrNull()
            }
        }
    }

    private fun updateJumpserver(jumpserver: JumpServer): Long? {
        jumpserver.run {
            jumpserverQueries.update(
                proxy_id = proxyId,
                jumpserver_order = order,
                jumpserver_userId = user?.userId,
                jumpsserver_serverId = server?.serverId,
                jumpserver_port = port,
                jumpserver_id = id
            )
        }
        return jumpserverQueries.getLastInsertedId().executeAsOneOrNull()
    }


    fun getProxy(id: Long): Proxy? {
        val proxy = proxyQueries.get(id).executeAsOneOrNull()
        if (proxy == null) return proxy

        val jumpservers = jumpserverQueries.getAllJumpserversByProxyId(id).executeAsList().map { it.toJumpServer() }

        return Proxy(
            proxy.proxy_id,
            proxy.proxy_name,
            jumpservers
        )
    }

    fun getAll(): Flow<List<Proxy>> {
        return proxyQueries.getAllProxyComplete().asFlow().map { query -> query.executeAsList().toProxy() }
    }

    suspend fun deleteProxy(proxy: Proxy) {
        withContext(coroutineDispatcher) {
            proxyQueries.delete(proxy.id)
        }
    }

    suspend fun deleteJumpserver(jumpserver: JumpServer) {
        withContext(coroutineDispatcher) {
            jumpserverQueries.delete(jumpserver.id)
        }
    }

    private fun List<GetAllProxyComplete>.toProxy(): List<Proxy> {
        val jumpservers = this.filter { it.jumpserver_id != null }.map {
            JumpServer(
                id = it.jumpserver_id!!,
                proxyId = it.proxy_id,
                order = it.jumpserver_order!!,
                user = it.jumpserver_userId?.let { id -> User(id, it.username!!) },
                server = it.jumpsserver_serverId?.let { id ->
                    Server(
                        serverId = id,
                        serverUrl = it.server_url!!,
                        title = it.title!!
                    )
                },
                port = it.jumpserver_port
            )
        }

        return this.distinctBy { it.proxy_id }.map {
            Proxy(
                id = it.proxy_id,
                title = it.proxy_name,
                jumpserverList = jumpservers.filter { server -> server.proxyId == it.proxy_id }
            )
        }
    }

}