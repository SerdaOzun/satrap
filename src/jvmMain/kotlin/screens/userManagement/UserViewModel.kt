package screens.userManagement

import AppDatabase
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import domain.Server
import domain.User
import domain.UserDataSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

/**
 * Operations to write and read from the database for users
 */
class UserViewModel(
    private val userDataSource: UserDataSource = AppDatabase.sqlUser
) : ViewModel() {

    val allUsers = userDataSource.getAllUser()
    var filteredUsers = mutableStateListOf<User>()

    //Selected users for the server which is currently being edited
    var selectedUsers by mutableStateOf(emptyList<User>())
        private set

    fun onEvent(userEvent: UserEvent): Long? {

        var userId: Long? = null

        viewModelScope.launch {
            when (userEvent) {
                is UserEvent.LoadUsers -> {
                    selectedUsers = if (userEvent.serverId == null) emptyList() else
                        userDataSource.getUsersByServerid(userEvent.serverId)
                }

                is UserEvent.InsertUser -> userId = userDataSource.insertUser(userEvent.user)
                is UserEvent.DeleteUser -> userDataSource.deleteUserById(userEvent.userId)
                is UserEvent.AddUserToServer -> userDataSource.addUserToServers(
                    userEvent.user.userId,
                    userEvent.server.serverId
                )

                is UserEvent.DeleteUserFromServer -> userDataSource.deleteUserFromServer(
                    userEvent.user.userId,
                    userEvent.server.serverId
                )

                is UserEvent.SelectUser -> {
                    //In case the user is not in the selectedUsers list add it
                    if (userEvent.user.userId !in selectedUsers.map { it.userId }) {
                        selectedUsers += userEvent.user
                    } else {
                        //Copy all users into temporary list again and only update the user which was passed to this function
                        selectedUsers = selectedUsers.map { u ->
                            if (u.userId == userEvent.user.userId) userEvent.user else u
                        }
                    }
                }

                is UserEvent.UnselectUser -> {
                    selectedUsers = selectedUsers.filterNot { it.userId == userEvent.user.userId }
                }

                is UserEvent.SaveServerConfiguration -> {
                    // Add the server to Users which are selected and remove it from those which are not
                    // Update UserServer Relation
                    selectedUsers.forEach {
                        if (it.serverIds.isEmpty() || !it.serverIds.contains(userEvent.serverId)) {
                            userDataSource.addUserToServers(it.userId, userEvent.serverId)
                        }
                    }
                    allUsers.first()
                        //filter users which have the server, but are not selected anymore and then delete the existing relation
                        .filter { u -> u.serverIds.isNotEmpty() && u.serverIds.contains(userEvent.serverId) && u.userId !in selectedUsers.map { it.userId } }
                        .forEach {
                            userDataSource.deleteUserFromServer(it.userId, userEvent.serverId)
                        }
                    //Update the users themselves
                    selectedUsers.forEach {
                        userDataSource.insertUser(it)
                    }
                }
            }
        }
        return userId
    }
}

sealed class UserEvent {
    data class LoadUsers(val serverId: Long?) : UserEvent()
    data class InsertUser(val user: User) : UserEvent()
    data class DeleteUser(val userId: Long) : UserEvent()
    data class SelectUser(val user: User) : UserEvent()
    data class UnselectUser(val user: User) : UserEvent()
    data class AddUserToServer(val user: User, val server: Server) : UserEvent()
    data class DeleteUserFromServer(val user: User, val server: Server) : UserEvent()
    data class SaveServerConfiguration(val serverId: Long) : UserEvent()
}