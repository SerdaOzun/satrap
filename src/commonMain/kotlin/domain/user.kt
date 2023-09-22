package domain

import satrapinsatrap.UserEntity

/**
 * Write and retrieve Users with which you can login into Servers
 * @see User for data
 */
interface UserDataSource {
    suspend fun insertUser(user: User)
    suspend fun getUserById(id: Long): User?
    suspend fun getAllUser(): List<User>
    suspend fun getUsersByServerid(serverId: Long): List<User>
    suspend fun deleteUserById(id: Long)
}

data class User(
    val userId: Long?,
    val serverId: Long?,
    val username: String,
    val role: String,
    val defaultUser: Boolean,
    val syncUser: Boolean,
    val userLevelDescription: String
) {
    override fun toString(): String {
        return username
    }
}

fun UserEntity.toUser() = User(
    user_id,
    server_id,
    username,
    role,
    defaultUser,
    syncUser,
    userLevelDescription
)