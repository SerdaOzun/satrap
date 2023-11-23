package util

import domain.ServerComplete
import screens.serverList.ServerViewModel
import screens.tagManagement.TagViewModel
import screens.userManagement.UserViewModel

/**
 * Only display servers according to the filters chosen
 * 1: Server URL or title contains the text being searched
 * 2: Server with the users which are selected
 * 3: Server with the tags which are selected
 */
fun List<ServerComplete>.filterSearch(serverVM: ServerViewModel, userVM: UserViewModel, tagVM: TagViewModel) = filter {
    it.server.serverUrl.lowercase().contains(serverVM.searchText.lowercase()) ||
            it.server.title.lowercase().contains(serverVM.searchText.lowercase())
}
    .filter { serverList ->
        if (userVM.filteredUsers.isNotEmpty()) {
            serverList.users.any { u ->
                userVM.filteredUsers.map { selectedUser -> selectedUser.username }.contains(u.username)
            }
        } else true
    }
    .filter { serverList ->
        if (tagVM.filteredTags.isNotEmpty()) {
            serverList.tags.any { t ->
                tagVM.filteredTags.map { selectedTag -> selectedTag.tag }.contains(t.tag)
            }
        } else true
    }