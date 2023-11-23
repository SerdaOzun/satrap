package data

import app.cash.sqldelight.coroutines.asFlow
import domain.Tag
import domain.TagDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import satrapco.satrap.Database
import satrapinsatrap.GetAllTags
import satrapinsatrap.GetTagById
import satrapinsatrap.GetTagsByServerId

class SqlDelightTag(db: Database) : TagDataSource {

    private val queries = db.tagQueries
    private val tagServerQueries = db.tagServerQueries

    override suspend fun insertTag(tag: Tag): Long? {
        val insertedTagId: Long? = queries.transactionWithResult {
            queries.insertTag(
                tag_id = tag.tagId,
                tag = tag.tag,
                syncTag = tag.syncTag
            )
            queries.getLastInsertedId().executeAsOneOrNull()
        }
        tag.serverIds?.forEach { serverId ->
            addTagToServer(insertedTagId!!, serverId)
        }
        return insertedTagId
    }

    override suspend fun getTagById(id: Long): Tag {
        return queries.getTagById(id).executeAsList().toTag().first()
    }

    override fun getAllTags(): Flow<List<Tag>> {
        return queries.getAllTags().asFlow().map { query -> query.executeAsList().toTag() }
    }

    override suspend fun getTagsByServerId(serverId: Long): List<Tag> {
        return queries.getTagsByServerId(serverId).executeAsList().toTag()
    }

    override suspend fun deleteTagById(id: Long) {
        queries.deleteTagById(id)
    }

    override suspend fun getLastInsertedId(): Long? {
        return queries.getLastInsertedId().executeAsOneOrNull()
    }

    override suspend fun addTagToServer(tagId: Long, serverId: Long) {
        tagServerQueries.insertRelation(tag_id = tagId, server_id = serverId)
    }

    override suspend fun removeTagFromServer(tagId: Long, serverId: Long) {
        tagServerQueries.deleteRelation(tagId = tagId, serverId = serverId)
    }
}


@JvmName("TagListToTag1")
private fun List<GetTagById>.toTag(): List<Tag> {
    val tagServersMap = this.groupBy { it.tag_id }.mapValues { (_, group) -> group.map { it.server_id } }

    return this.distinctBy { it.tag_id }.map {
        Tag(
            it.tag_id,
            tagServersMap[it.tag_id],
            it.tag,
            it.syncTag
        )
    }
}

@JvmName("TagListToTag2")
private fun List<GetAllTags>.toTag(): List<Tag> {
    val tagServersMap = this.groupBy { it.tag_id }.mapValues { (_, group) -> group.mapNotNull { it.server_id } }

    return this.distinctBy { it.tag_id }.map {
        Tag(
            it.tag_id,
            tagServersMap[it.tag_id],
            it.tag,
            it.syncTag
        )
    }
}

@JvmName("TagListToTag3")
private fun List<GetTagsByServerId>.toTag(): List<Tag> {
    val tagServersMap = this.groupBy { it.tag_id }.mapValues { (_, group) -> group.mapNotNull { it.server_id } }

    return this.distinctBy { it.tag_id }.map {
        Tag(
            it.tag_id,
            tagServersMap[it.tag_id],
            it.tag,
            it.syncTag
        )
    }
}
