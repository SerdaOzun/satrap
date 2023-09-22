package data

import domain.Tag
import domain.TagDataSource
import domain.toTag
import satrapco.satrap.Database

class SqlDelightTag(db: Database) : TagDataSource {

    private val queries = db.tagsQueries
    override suspend fun insertTag(tag: Tag) {
        tag.let {
            queries.insertTag(it.tagId, it.serverId!!, it.tag, it.syncTag)
        }
    }

    override suspend fun getTagById(id: Long): Tag? {
        return queries.getTagById(id).executeAsOneOrNull()?.toTag()
    }

    override suspend fun getAllTag(): List<Tag> {
        return queries.getAllTags().executeAsList().map { it.toTag() }
    }

    override suspend fun getTagsByServerId(serverId: Long): List<Tag> {
        return queries.getTagsByServerId(serverId).executeAsList().map { it.toTag() }
    }

    override suspend fun deleteTagById(id: Long) {
        queries.deleteTagById(id)
    }
}