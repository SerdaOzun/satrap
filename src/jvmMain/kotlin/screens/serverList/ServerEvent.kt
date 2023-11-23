package screens.serverList

import domain.Server

sealed class ServerEvent {
    data class InitializeServer(val id: Long?): ServerEvent()
    data class InsertServer(val server: Server): ServerEvent()
    data class UpdateDefaultUser(val userId: Long?): ServerEvent()
    data class DeleteServer(val serverId: Long): ServerEvent()
}