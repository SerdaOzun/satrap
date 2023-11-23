package screens.tagManagement

import AppDatabase
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import domain.Tag
import domain.TagDataSource
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class TagViewModel(
    private val tagDataSource: TagDataSource = AppDatabase.sqlDelightTag
) : ViewModel() {

    val allTags = tagDataSource.getAllTags()
    var filteredTags = mutableStateListOf<Tag>()

    var selectedTags by mutableStateOf(emptyList<Tag>())
        private set

    fun onEvent(tagEvent: TagEvent): Long? {

        suspend fun loadTags(tagId: Long?): List<Tag> {
            return viewModelScope.async {
                if (tagId == null) emptyList() else
                    tagDataSource.getTagsByServerId(tagId)
            }.await()
        }

        suspend fun insertTag(tag: Tag): Long? {
            return viewModelScope.async {
                tagDataSource.insertTag(tag)
            }.await()
        }

        var tagId: Long? = null
        viewModelScope.launch {
            when (tagEvent) {
                is TagEvent.LoadTags -> {
                    selectedTags = loadTags(tagEvent.serverId)
                }

                is TagEvent.InsertTag -> tagId = insertTag(tagEvent.tag)
                is TagEvent.DeleteTag -> tagDataSource.deleteTagById(tagEvent.tag.tagId!!)
                is TagEvent.SelectTag -> {
                    if (tagEvent.tag.tagId !in selectedTags.map { it.tagId }) {
                        selectedTags += tagEvent.tag
                    } else {
                        selectedTags = selectedTags.map { t ->
                            if (t.tagId == tagEvent.tag.tagId) tagEvent.tag else t
                        }
                    }
                }

                is TagEvent.UnselectTag -> selectedTags = selectedTags.filterNot { it.tagId == tagEvent.tag.tagId }
                is TagEvent.AddTagToServer -> tagDataSource.addTagToServer(
                    tagEvent.tag.tagId!!,
                    tagEvent.server.serverId!!
                )

                is TagEvent.DeleteTagFromServer -> tagDataSource.removeTagFromServer(
                    tagEvent.tag.tagId!!,
                    tagEvent.server.serverId!!
                )

                is TagEvent.SaveServerConfiguration -> {
                    // Add the server to Tags which are selected and remove it from those which are not
                    // Update TagServer Relation
                    selectedTags.forEach {
                        if (it.serverIds == null || !it.serverIds.contains(tagEvent.serverId)) {
                            tagDataSource.addTagToServer(it.tagId!!, tagEvent.serverId)
                        }
                    }
                    allTags.first()
                        //filter tags which have the server, but are not selected anymore and then delete the existing relation
                        .filter { t -> t.serverIds != null && t.serverIds.contains(tagEvent.serverId) && t.tagId !in selectedTags.map { it.tagId } }
                        .forEach {
                            tagDataSource.removeTagFromServer(it.tagId!!, tagEvent.serverId)
                        }
                    //Update the tags themselves
                    selectedTags.forEach {
                        tagDataSource.insertTag(it)
                    }
                }
            }
        }
        return tagId
    }

}


