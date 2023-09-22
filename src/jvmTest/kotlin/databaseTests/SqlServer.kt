package databaseTests

import DatabaseTestCase
import Testdata
import io.kotest.assertions.withClue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.testng.annotations.Test


/**
 * Insert, Read and Delete a server from the database
 */
class SqlServer : DatabaseTestCase() {

    private var serverId: Long? = -1
    private val server = Testdata.server

    @Test
    fun `Insert server`() {
        runBlocking {
            withClue("Insert a server into the database. The database should contain one Server") {
                serverDataSource.insertServer(server)
                serverDataSource.getAllServer().first().size shouldBe 1
            }
        }
    }

    @Test(dependsOnMethods = ["Insert server"])
    fun `Get server`() {
        runBlocking {
            serverId = serverDataSource.getLastInsertedId()

            serverDataSource.getServerById(serverId!!)!!.apply {
                serverUrl shouldBe server.serverUrl
                title shouldBe server.title
                organization shouldBe server.organization
                description shouldBe server.description
                syncServer shouldBe server.syncServer
            }
        }
    }

    @Test(dependsOnMethods = ["Get server"])
    fun `Delete server`() {
        runBlocking {
            serverDataSource.deleteServerById(serverId!!)
            serverDataSource.getAllServer().first().shouldBeEmpty()
        }
    }
}