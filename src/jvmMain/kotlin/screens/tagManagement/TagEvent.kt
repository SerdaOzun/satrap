package screens.tagManagement

import domain.Server
import domain.Tag

sealed class TagEvent {
    data class LoadTags(val serverId: Long?): TagEvent()
    data class InsertTag(val tag: Tag) : TagEvent()
    data class DeleteTag(val tag: Tag) : TagEvent()
    data class SelectTag(val tag: Tag) : TagEvent()
    data class UnselectTag(val tag: Tag) : TagEvent()
    data class AddTagToServer(val tag: Tag, val server: Server): TagEvent()
    data class DeleteTagFromServer(val tag: Tag, val server: Server): TagEvent()
    data class SaveServerConfiguration(val serverId: Long): TagEvent()
}
