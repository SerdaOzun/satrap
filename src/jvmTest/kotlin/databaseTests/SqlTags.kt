package databaseTests

import DatabaseTestCase
import Testdata
import domain.Tag
import io.kotest.assertions.withClue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.testng.annotations.Test

class SqlTags : DatabaseTestCase() {

    private val server = Testdata.server
    private val tagWithoutServerId = Testdata.tagWithoutServerId
    private lateinit var tagWithServerId: Tag

    @Test
    fun `Insert tag without server Id`() {
        runBlocking {
            withClue("Insert a tag without a serverId. The tag is saved and has an empty list of servers") {
                tagDataSource.insertTag(tagWithoutServerId)

                tagDataSource.getAllTags().first().let {
                    it.size shouldBe 1
                    it.first().serverIds.shouldBeEmpty()
                }
            }
        }
    }

    @Test(dependsOnMethods = ["Insert tag without server Id"])
    fun `Insert tag`() {
        runBlocking {
            withClue("Insert a server and a tag with a server Id into the database. The database should contain one tag") {
                serverDataSource.insertServer(server)
                val serverId = serverDataSource.getLastInsertedId()!!

                tagWithServerId = Tag(
                    tagId = null,
                    serverIds = listOf(serverId),
                    tag = "Tag with serverId",
                    syncTag = true
                )
                tagDataSource.insertTag(tagWithServerId)
                tagDataSource.getAllTags().first().size shouldBe 2
            }
        }
    }

    @Test(dependsOnMethods = ["Insert tag"])
    fun `Get tags`() {
        runBlocking {
            tagDataSource.getTagsByServerId(tagWithServerId.serverIds!!.first()).size shouldBe 1

            tagDataSource.getAllTags().first().last().let {
                it.serverIds shouldContainExactly tagWithServerId.serverIds!!
                it.tag shouldBe tagWithServerId.tag
                it.syncTag shouldBe tagWithServerId.syncTag
            }
        }
    }

    @Test(dependsOnMethods = ["Get tags"])
    fun `Delete tag`() {
        runBlocking {
            tagDataSource.getAllTags().first().forEach {
                tagDataSource.deleteTagById(it.tagId!!)
            }
            tagDataSource.getAllTags().first().shouldBeEmpty()
        }
    }
}