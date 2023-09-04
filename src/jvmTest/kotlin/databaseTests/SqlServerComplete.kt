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
                sync = it % 2 == 0,
                customServer = it % 2 == 0
            )
        }
        runBlocking {
            servers.forEach {
                serverDataSource.insertServer(it)
            }

            serverDataSource.getAllServer().first().forEachIndexed { index, server ->
                (0..index).map {
                    tagDataSource.insertTag(
                        Tag(null, server.serverId!!, tag = "tag$it", customTag = it % 2 == 0)
                    )
                }
                (0..index).map {
                    userDataSource.insertUser(
                        User(
                            null,
                            server.serverId!!,
                            "username$it",
                            "role$it",
                            it % 2 == 0,
                            "desc$it"
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
                serverDataSource.getAllServerComplete().first().forOne { serverComplete ->
                    serverComplete.server.title shouldBe "title$index"
                    serverComplete.tags.map { it.tag } shouldContainAll (0..index).map { tagIndex -> "tag$tagIndex" }
                    serverComplete.users.map { it.username } shouldContainAll (0..index).map { userIndex -> "username$userIndex" }
                }
            }
        }
    }
}