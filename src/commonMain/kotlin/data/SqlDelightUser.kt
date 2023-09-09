package data

import domain.User
import domain.UserDataSource
import domain.toUser
import satrapco.satrap.Database

class SqlDelightUser(db: Database) : UserDataSource {

    private val queries = db.userQueries

    override suspend fun insertUser(user: User) {
        with(user) {
            queries.insertUser(userId, serverId!!, username, role, customUser, userLevelDescription)
        }
    }

    override suspend fun getUserById(id: Long): User? {
        return queries.getUserById(id).executeAsOneOrNull()?.toUser()
    }

    override suspend fun getAllUser(): List<User> {
        return queries.getAllUser().executeAsList().map { it.toUser() }
    }

    override suspend fun getUsersByServerid(serverId: Long): List<User> {
        return queries.getUsersByServerId(serverId).executeAsList().map { it.toUser() }
    }

    override suspend fun deleteUserById(id: Long) {
        queries.deleteUserById(id)
    }

}