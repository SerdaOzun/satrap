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
    val userId: Long?,
    val serverIds: List<Long>?,
    val username: String,
    val role: String,
    val defaultUser: Boolean,
    val syncUser: Boolean,
    val userLevelDescription: String
) {
    override fun toString(): String {
        return username
    }

    constructor(username: String) : this(null, emptyList(), username, "", false, false, "")

}