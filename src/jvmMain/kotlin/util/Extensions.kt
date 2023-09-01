package util

import domain.ServerComplete
import serverList.ServerViewModel

/**
 * Only display servers according to the filters chosen
 * 1: Server URL or title contains the text being searched
 * 2: Server with the users which are selected
 * 3: Server with the tags which are selected
 */
fun List<ServerComplete>.filterSearch(serverVM: ServerViewModel) = filter {
    it.server.serverUrl.lowercase().contains(serverVM.searchText.lowercase()) ||
            it.server.title.lowercase().contains(serverVM.searchText.lowercase())
}
    .filter { serverList ->
        if (serverVM.selectedUsers.isNotEmpty()) {
            serverList.users.any { u ->
                serverVM.selectedUsers.map { selectedUser -> selectedUser.username }.contains(u.username)
            }
        } else true
    }
    .filter { serverList ->
        if (serverVM.selectedTags.isNotEmpty()) {
            serverList.tags.any { t ->
                serverVM.selectedTags.map { selectedTag -> selectedTag.tag }.contains(t.tag)
            }
        } else true
    }