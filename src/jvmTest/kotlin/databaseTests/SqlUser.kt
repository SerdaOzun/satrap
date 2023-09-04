package databaseTests

import DatabaseTestCase
import Testdata
import domain.User
import io.kotest.assertions.withClue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.testng.annotations.Test

class SqlUser : DatabaseTestCase() {

    private val server = Testdata.server
    private val userWithoutserverId = Testdata.userWithoutServerId
    private lateinit var userWithServerId: User

    @Test(expectedExceptions = [NullPointerException::class])
    fun `Insert user without server Id`() {
        runBlocking {
            withClue("Insert a user without a serverId. An exception is thrown") {
                userDataSource.insertUser(userWithoutserverId)
            }
        }
    }

    @Test(dependsOnMethods = ["Insert user without server Id"])
    fun `Insert user`() {
        runBlocking {
            withClue("Insert a server and a user with a server Id into the database. The database should contain one user") {
                serverDataSource.insertServer(server)
                val serverId = serverDataSource.getLastInsertedId()

                userWithServerId = User(
                    null,
                    serverId,
                    username = "User with server Id",
                    role = "Admin",
                    customUser = true,
                    userLevelDescription = "All privileges"
                )
                userDataSource.insertUser(userWithServerId)
                userDataSource.getAllUser().size shouldBe 1
            }
        }
    }

    @Test(dependsOnMethods = ["Insert user"])
    fun `Get users`() {
        runBlocking {
            userDataSource.getUsersByServerid(userWithServerId.serverId!!).size shouldBe 1

            userDataSource.getAllUser().single().let {
                it.serverId shouldBe userWithServerId.serverId
                it.username shouldBe userWithServerId.username
                it.role shouldBe userWithServerId.role
                it.customUser shouldBe userWithServerId.customUser
                it.userLevelDescription shouldBe userWithServerId.userLevelDescription
            }
        }
    }

    @Test(dependsOnMethods = ["Get users"])
    fun `Delete user`() {
        runBlocking {
            userDataSource.deleteUserById(userDataSource.getAllUser().first().userId!!)
            userDataSource.getAllUser().shouldBeEmpty()
        }
    }
}