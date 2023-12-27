package data

import app.cash.sqldelight.coroutines.asFlow
import domain.Tag
import domain.TagDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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

    override suspend fun insert(tag: Tag): Long? {
        return withContext(coroutineDispatcher) {
            async {
                queries.transactionWithResult {
                    queries.insertTag(
                        tag_id = tag.tagId,
                        tag = tag.tag,
                        syncTag = tag.syncTag
                    )
                    queries.getLastInsertedId().executeAsOneOrNull()
                }
            }.await()?.let { insertedTagId ->
                tag.serverIds?.forEach { serverId ->
                    withContext(Dispatchers.IO) {
                        addTagToServer(insertedTagId, serverId)
                    }
                }
                insertedTagId
            }
        }
    }

    override suspend fun get(id: Long): Tag {
        return withContext(coroutineDispatcher) {
            async {
                queries.getTagById(id).executeAsList().toTag().first()
            }
        }.await()
    }

    override fun getAll(): Flow<List<Tag>> {
        return queries.getAllTags().asFlow().map { query -> query.executeAsList().toTag() }
    }

    override suspend fun getAllByServerId(serverId: Long): List<Tag> {
        return withContext(coroutineDispatcher) {
            async {
                queries.getTagsByServerId(serverId).executeAsList().toTag()
            }
        }.await()
    }

    override suspend fun delete(id: Long) {
        withContext(coroutineDispatcher) { queries.deleteTagById(id) }
    }

    override suspend fun getLastInsertedId(): Long? {
        return withContext(coroutineDispatcher) {
            async {
                queries.getLastInsertedId().executeAsOneOrNull()
            }
        }.await()
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
