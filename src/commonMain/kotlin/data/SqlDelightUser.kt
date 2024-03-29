package data

import app.cash.sqldelight.coroutines.asFlow
import domain.User
import domain.UserDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import satrapco.satrap.Database
import satrapinsatrap.GetAllUser
import satrapinsatrap.GetUserById
import satrapinsatrap.GetUsersByServerId

class SqlDelightUser(
    db: Database,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserDataSource {

    private val queries = db.userQueries
    private val userServerQueries = db.userServerQueries

    override suspend fun insertUser(user: User): Long? = user.run {
        if (userId >= 0) {
            updateUser(user)
        } else {
            queries.transactionWithResult {
                queries.insertUser(
                    user_id = null,
                    username = username,
                    role = role,
                    defaultUser = defaultUser,
                    syncUser = syncUser,
                    userLevelDescription = userLevelDescription
                )
                queries.getLastInsertedId().executeAsOneOrNull()
            }?.let { insertedUserId ->
                serverIds.forEach { serverId ->
                    addUserToServers(userId = insertedUserId, serverId = serverId)
                }
                insertedUserId
            }
        }
    }

    private fun updateUser(user: User): Long {
        user.run {
            queries.updateUser(
                username = username,
                role = role,
                defaultUser = defaultUser,
                syncUser = syncUser,
                userLevelDescription = userLevelDescription,
                user_id = userId,
            )
        }
        return user.userId
    }

    override suspend fun getUserById(id: Long): User {
        return queries.getUserById(id).executeAsList().toUser().first()
    }

    override fun getAllUser(): Flow<List<User>> {
        return queries.getAllUser().asFlow().map { query -> query.executeAsList().toUser() }
    }

    override suspend fun getUsersByServerid(serverId: Long): List<User> {
        return queries.getUsersByServerId(serverId).executeAsList().toUser()
    }

    override suspend fun deleteUserById(id: Long) {
        withContext(coroutineDispatcher) { queries.deleteUserById(id) }
    }

    override suspend fun addUserToServers(userId: Long, serverId: Long) {
        withContext(coroutineDispatcher) { userServerQueries.insertRelation(user_id = userId, server_id = serverId) }
    }

    override suspend fun deleteUserFromServer(userId: Long, serverId: Long) {
        withContext(coroutineDispatcher) { userServerQueries.deleteRelation(userId, serverId) }
    }

    override suspend fun getLastInsertedId(): Long? {
        return queries.getLastInsertedId().executeAsOneOrNull()
    }
}

@JvmName("UserListToUser1")
private fun List<GetUserById>.toUser(): List<User> {
    val userServersMap = this.groupBy { it.user_id }.mapValues { (_, group) -> group.map { it.server_id } }

    return this.distinctBy { it.user_id }.map {
        User(
            it.user_id,
            userServersMap[it.user_id] ?: emptyList(),
            it.username,
            it.role,
            it.defaultUser,
            it.syncUser,
            it.userLevelDescription
        )
    }
}

@JvmName("UserListToUser2")
private fun List<GetAllUser>.toUser(): List<User> {
    val userServersMap = this.groupBy { it.user_id }.mapValues { (_, group) -> group.mapNotNull { it.server_id } }

    return this.distinctBy { it.user_id }.map {
        User(
            it.user_id,
            userServersMap[it.user_id] ?: emptyList(),
            it.username,
            it.role,
            it.defaultUser,
            it.syncUser,
            it.userLevelDescription
        )
    }
}

@JvmName("UserListToUser3")
private fun List<GetUsersByServerId>.toUser(): List<User> {
    val userServersMap = this.groupBy { it.user_id }.mapValues { (_, group) -> group.mapNotNull { it.server_id } }

    return this.distinctBy { it.user_id }.map {
        User(
            it.user_id,
            userServersMap[it.user_id] ?: emptyList(),
            it.username,
            it.role,
            it.defaultUser,
            it.syncUser,
            it.userLevelDescription
        )
    }
}