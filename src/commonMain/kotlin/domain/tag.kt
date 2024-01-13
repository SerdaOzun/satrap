package domain

import androidx.compose.runtime.Immutable
import kotlinx.coroutines.flow.Flow

/**
 * Write and retrieve Tags which allow Users to organize their servers
 * @see Tag
 */
interface TagDataSource {
    suspend fun insert(tag: Tag): Long?
    suspend fun get(id: Long): Tag?
    fun getAll(): Flow<List<Tag>>
    suspend fun getAllByServerId(serverId: Long): List<Tag>
    suspend fun addTagToServer(tagId: Long, serverId: Long)
    suspend fun removeTagFromServer(tagId: Long, serverId: Long)
    suspend fun delete(id: Long)
    suspend fun getLastInsertedId(): Long?
}

@Immutable
data class Tag(
    val tagId: Long,
    val serverIds: List<Long>,
    val tag: String,
    val syncTag: Boolean
) {
    override fun toString(): String {
        return tag
    }

    constructor(tag: String) : this(-1L, emptyList(), tag, false)
    constructor(tag: String, syncTag: Boolean) : this(-1L, emptyList(), tag, syncTag)
    constructor(serverIds: List<Long>, tag: String, syncTag: Boolean) : this(-1L, serverIds, tag, syncTag)
}
