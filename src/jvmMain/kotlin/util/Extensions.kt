package util

import domain.ServerComplete
import serverList.ServerViewModel

fun List<ServerComplete>.filterSearch(serverVM: ServerViewModel) = filter {
    it.server.serverUrl.lowercase().contains(serverVM.searchText.lowercase()) ||
            it.server.title.lowercase().contains(serverVM.searchText.lowercase())
}
    .filter { serverList ->
        if (serverVM.selectedUsers.isNotEmpty()) {
            serverList.users.any { u ->
                if (serverVM.selectedUsers.isNotEmpty()) {
                    serverVM.selectedUsers.map { selectedUser -> selectedUser.username }.contains(u.username)
                } else true
            }
        } else true
    }
    .filter { serverList ->
        if (serverVM.selectedTags.isNotEmpty()) {
            serverList.tags.any { t ->
                if (serverVM.selectedTags.isNotEmpty()) {
                    serverVM.selectedTags.contains(t)
                } else true
            }
        } else true
    }