package screens.serverList

import domain.Server
import domain.Tag
import domain.User

sealed class ServerEvent {
    data class InitializeServer(val id: Long?): ServerEvent()
    data class InsertServer(val server: Server): ServerEvent()
    data class DeleteServer(val serverId: Long): ServerEvent()
    data class GetServer(val serverId: Long): ServerEvent()
    data class InsertUser(val user: User?, val userIndex: Int = -1): ServerEvent()
    data class DeleteUser(val userIndex: Int): ServerEvent()
    data class InsertTag(val tag: Tag?, val tagindex: Int = -1): ServerEvent()
    data class DeleteTag(val tagIndex: Int): ServerEvent()
}