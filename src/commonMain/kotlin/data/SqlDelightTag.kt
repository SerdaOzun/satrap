package data

import app.cash.sqldelight.coroutines.asFlow
import domain.Tag
import domain.TagDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import satrapco.satrap.Database
import satrapinsatrap.GetAllTags
import satrapinsatrap.GetTagById
import satrapinsatrap.GetTagsByServerId

class SqlDelightTag(
    db: Database,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TagDataSource {

    private val queries = db.tagQueries
    private val tagServerQueries = db.tagServerQueries

    override suspend fun insert(tag: Tag): Long? = tag.run {
        if (tagId >= 0) {
            update(tag)
        } else {
            queries.transactionWithResult {
                queries.insertTag(
                    tag_id = if (tag.tagId < 0) null else tag.tagId,
                    tag = tag.tag,
                    syncTag = tag.syncTag
                )
                queries.getLastInsertedId().executeAsOneOrNull()
            }?.let { insertedTagId ->
                tag.serverIds.forEach { serverId ->
                    withContext(Dispatchers.IO) {
                        addTagToServer(insertedTagId, serverId)
                    }
                }
                insertedTagId
            }
        }
    }

    private fun update(tag: Tag): Long {
        queries.updateTag(tag = tag.tag, syncTag = tag.syncTag, tag_id = tag.tagId)
        return tag.tagId
    }

    override suspend fun get(id: Long): Tag {
        return queries.getTagById(id).executeAsList().toTag().first()
    }

    override fun getAll(): Flow<List<Tag>> {
        return queries.getAllTags().asFlow().map { query -> query.executeAsList().toTag() }
    }

    override suspend fun getAllByServerId(serverId: Long): List<Tag> {
        return queries.getTagsByServerId(serverId).executeAsList().toTag()
    }

    override suspend fun delete(id: Long) {
        withContext(coroutineDispatcher) { queries.deleteTagById(id) }
    }

    override suspend fun getLastInsertedId(): Long? {
        return queries.getLastInsertedId().executeAsOneOrNull()
    }

    override suspend fun addTagToServer(tagId: Long, serverId: Long) {
        withContext(coroutineDispatcher) { tagServerQueries.insertRelation(tag_id = tagId, server_id = serverId) }
    }

    override suspend fun removeTagFromServer(tagId: Long, serverId: Long) {
        withContext(coroutineDispatcher) { tagServerQueries.deleteRelation(tagId = tagId, serverId = serverId) }
    }
}


@JvmName("TagListToTag1")
private fun List<GetTagById>.toTag(): List<Tag> {
    val tagServersMap = this.groupBy { it.tag_id }.mapValues { (_, group) -> group.map { it.server_id } }

    return this.distinctBy { it.tag_id }.map {
        Tag(
            it.tag_id,
            tagServersMap[it.tag_id] ?: emptyList(),
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
            tagServersMap[it.tag_id] ?: emptyList(),
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
            tagServersMap[it.tag_id] ?: emptyList(),
            it.tag,
            it.syncTag
        )
    }
}
