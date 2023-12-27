package databaseTests

import DatabaseTestCase
import Testdata
import domain.User
import io.kotest.assertions.withClue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.testng.annotations.Test

class SqlUser : DatabaseTestCase() {

    private val server = Testdata.server
    private val userWithoutserverId = Testdata.userWithoutServerId
    private lateinit var userWithServerId: User

    @Test
    fun `Insert user without server Id`() {
        runBlocking {
            withClue("Insert a user without a serverId. The user is saved and has an empty list of servers") {
                userDataSource.insertUser(userWithoutserverId)

                userDataSource.getAllUser().first().let {
                    it.size shouldBe 1
                    it.first().serverIds.shouldBeEmpty()
                }
            }
        }
    }

    @Test(dependsOnMethods = ["Insert user without server Id"])
    fun `Insert user`() {
        runBlocking {
            withClue("Insert a server and a user with a server Id into the database. The database should contain one user") {
                serverDataSource.insert(server)
                val serverId = serverDataSource.getLastInsertedId()

                userWithServerId = User(
                    null,
                    listOf(serverId!!),
                    username = "User with server Id",
                    role = "Admin",
                    defaultUser = true,
                    syncUser = true,
                    userLevelDescription = "All privileges"
                )
                userDataSource.insertUser(userWithServerId)
                userDataSource.getAllUser().first().size shouldBe 2
            }
        }
    }

    @Test(dependsOnMethods = ["Insert user"])
    fun `Get users`() {
        runBlocking {
            userDataSource.getUsersByServerid(userWithServerId.serverIds!!.first()).size shouldBe 1

            userDataSource.getAllUser().first().first { it.serverIds != null }.let {
                it.serverIds shouldContainExactly userWithServerId.serverIds!!
                it.username shouldBe userWithServerId.username
                it.role shouldBe userWithServerId.role
                it.defaultUser shouldBe userWithServerId.defaultUser
                it.syncUser shouldBe userWithServerId.syncUser
                it.userLevelDescription shouldBe userWithServerId.userLevelDescription
            }
        }
    }

    @Test(dependsOnMethods = ["Get users"])
    fun `Delete user`() {
        runBlocking {
            userDataSource.getAllUser().first().forEach {
                userDataSource.deleteUserById(it.userId!!)
            }
            userDataSource.getAllUser().first().shouldBeEmpty()
        }
    }
}