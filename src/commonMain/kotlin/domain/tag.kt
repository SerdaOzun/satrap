package domain

import satrapinsatrap.TagEntity


interface TagDataSource {
    suspend fun insertTag(tag: Tag)
    suspend fun getTagById(id: Long): Tag?
    suspend fun getAllTag(): List<Tag>
    suspend fun getTagsByServerId(serverId: Long): List<Tag>
    suspend fun deleteTagById(id: Long)
}

data class Tag(
    val tagId: Long?,
    val serverId: Long?,
    val tag: String,
    val customTag: Boolean
) {
    override fun toString(): String {
        return tag
    }
}

fun TagEntity.toTag() = Tag(
    tag_id,
    server_id,
    tag,
    customTag
)