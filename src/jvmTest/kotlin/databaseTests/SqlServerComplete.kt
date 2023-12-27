package databaseTests

import DatabaseTestCase
import domain.Server
import domain.Tag
import domain.User
import io.kotest.inspectors.forOne
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

class SqlServerComplete : DatabaseTestCase() {

    @BeforeClass
    fun createData() {
        val servers = (0..4).map {
            Server(
                null,
                "url$it",
                title = "title$it",
                organization = "org$it",
                description = "desc$it",
                syncServer = it % 2 == 0,
                defaultUserId = null
            )
        }
        runBlocking {
            servers.forEach {
                serverDataSource.insert(it)
            }

            serverDataSource.getServer().first().forEachIndexed { index, server ->
                (0..index).map {
                    tagDataSource.insert(
                        Tag(tagId = null, serverIds = listOf(server.serverId!!), tag = "tag$it", syncTag = it % 2 == 0)
                    )
                }
                (0..index).map {
                    userDataSource.insertUser(
                        User(
                            userId = null,
                            serverIds = listOf(server.serverId!!),
                            username = "username$it",
                            role = "role$it",
                            defaultUser = it % 2 == 0,
                            syncUser = false,
                            userLevelDescription = "desc$it"
                        )

                    )
                }
            }
        }
    }

    @Test
    fun `Get all Server Completes`() {
        runBlocking {
            (0..4).forEach { index ->
                serverCompleteDataSource.getAll().first().forOne { serverComplete ->
                    serverComplete.server.title shouldBe "title$index"
                    serverComplete.tags.map { it.tag } shouldContainAll (0..index).map { tagIndex -> "tag$tagIndex" }
                    serverComplete.users.map { it.username } shouldContainAll (0..index).map { userIndex -> "username$userIndex" }
                }
            }
        }
    }
}