package databaseTests

import DatabaseTestCase
import Testdata
import domain.Tag
import io.kotest.assertions.withClue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.testng.annotations.Test

class SqlTags : DatabaseTestCase() {

    private val server = Testdata.server
    private val tagWithoutServerId = Testdata.tagWithoutServerId
    private lateinit var tagWithServerId: Tag

    @Test(expectedExceptions = [NullPointerException::class])
    fun `Insert tag without server Id`() {
        runBlocking {
            withClue("Insert a tag without a serverId. An exception is thrown") {
                tagDataSource.insertTag(tagWithoutServerId)
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
                    serverId = serverId,
                    tag = "Tag with serverId",
                    syncTag = true
                )
                tagDataSource.insertTag(tagWithServerId)
                tagDataSource.getAllTag().size shouldBe 1
            }
        }
    }

    @Test(dependsOnMethods = ["Insert tag"])
    fun `Get tags`() {
        runBlocking {
            tagDataSource.getTagsByServerId(tagWithServerId.serverId!!).size shouldBe 1

            tagDataSource.getAllTag().single().let {
                it.serverId shouldBe tagWithServerId.serverId
                it.tag shouldBe tagWithServerId.tag
                it.syncTag shouldBe tagWithServerId.syncTag
            }
        }
    }

    @Test(dependsOnMethods = ["Get tags"])
    fun `Delete tag`() {
        runBlocking {
            tagDataSource.deleteTagById(tagDataSource.getAllTag().first().tagId!!)
            tagDataSource.getAllTag().shouldBeEmpty()
        }
    }
}