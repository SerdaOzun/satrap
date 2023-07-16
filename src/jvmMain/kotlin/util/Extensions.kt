package util

import domain.ServerComplete
import serverList.ServerViewModel

fun List<ServerComplete>.filterSearch(serverVM: ServerViewModel) = filter {
    it.server.serverUrl.lowercase().contains(serverVM.searchText.lowercase()) ||
            it.server.title.lowercase().contains(serverVM.searchText.lowercase())
}
    .filter {
        if (serverVM.selectedUsers.isNotEmpty()) {
            it.users.any { u ->
                if (serverVM.selectedUsers.isNotEmpty()) {
                    serverVM.selectedUsers.contains(u)
                } else true
            }
        } else true
    }
    .filter {
        if (serverVM.selectedTags.isNotEmpty()) {
            it.tags.any { t ->
                if (serverVM.selectedTags.isNotEmpty()) {
                    serverVM.selectedTags.contains(t)
                } else true
            }
        } else true
    }