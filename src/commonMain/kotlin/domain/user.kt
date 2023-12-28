package domain

import kotlinx.coroutines.flow.Flow

/**
 * Write and retrieve Users with which you can login into Servers
 * @see User for data
 */
interface UserDataSource {
    suspend fun insertUser(user: User): Long?
    suspend fun getUserById(id: Long): User?
    fun getAllUser(): Flow<List<User>>
    suspend fun getUsersByServerid(serverId: Long): List<User>
    suspend fun deleteUserById(id: Long)
    suspend fun addUserToServers(userId: Long, serverId: Long)
    suspend fun deleteUserFromServer(userId: Long, serverId: Long)
    suspend fun getLastInsertedId(): Long?
}

data class User(
    val userId: Long,
    val serverIds: List<Long>,
    val username: String,
    val role: String,
    val defaultUser: Boolean,
    val syncUser: Boolean,
    val userLevelDescription: String
) {
    override fun toString(): String {
        return username
    }

    constructor(username: String) : this(-1L, emptyList(), username, "", false, false, "")
    constructor(
        username: String,
        role: String,
        defaultUser: Boolean,
        syncUser: Boolean,
        userLevelDescription: String
    ) : this(-1L, emptyList(), username, role, defaultUser, syncUser, userLevelDescription)
    constructor(
        serverIds: List<Long>,
        username: String,
        role: String,
        defaultUser: Boolean,
        syncUser: Boolean,
        userLevelDescription: String
    ) : this(-1L, serverIds, username, role, defaultUser, syncUser, userLevelDescription)
}