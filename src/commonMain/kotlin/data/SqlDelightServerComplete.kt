package data

import app.cash.sqldelight.coroutines.asFlow
import domain.Server
import domain.ServerComplete
import domain.Tag
import domain.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import satrapco.satrap.Database
import satrapinsatrap.Servercomplete

class SqlDelightServerComplete(db: Database) {
    private val queries = db.servercompleteQueries

    fun getAll(): Flow<List<ServerComplete>> {
        return queries.getAllServerComplete().asFlow().map { query -> query.executeAsList().toServerComplete() }

    }

    fun get(id: Long): ServerComplete? {
        return queries.getServerCompleteById(id).executeAsList().toServerComplete().firstOrNull()
    }
}

private fun List<Servercomplete>.toServerComplete(): List<ServerComplete> {
    val tagServerMap = this.groupBy { it.tag_id }.mapValues { (_, group) -> group.map { it.server_id } }
    val tags = this.filter { it.tag_id != null }.map {
        Tag(
            tagId = it.tag_id!!,
            serverIds = tagServerMap[it.tag_id] ?: emptyList(),
            tag = it.tag!!,
            syncTag = it.syncTag!!
        )
    }.distinct()

    val userServersMap = this.groupBy { it.user_id }.mapValues { (_, group) -> group.map { it.server_id } }
    val user = this.filter { it.user_id != null }.map {
        User(
            userId = it.user_id!!,
            serverIds = userServersMap[it.user_id] ?: emptyList(),
            username = it.username!!,
            role = it.role!!,
            defaultUser = it.defaultUser!!,
            syncUser = it.syncUser!!,
            userLevelDescription = it.userLevelDescription!!
        )
    }.distinct()

    return this.distinctBy { it.server_id }.map {
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
            tags.filter { tag -> tag.serverIds.contains(it.server_id) },
            user.filter { user -> user.serverIds.contains(it.server_id) }
        )
    }
}