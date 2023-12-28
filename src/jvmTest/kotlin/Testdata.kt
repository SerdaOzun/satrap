import domain.Server
import domain.Tag
import domain.User

object Testdata {
    val server = Server(
        serverUrl = "my-server-url",
        title = "server-title",
        organization = "Testorg",
        description = "test",
        syncServer = false,
        defaultUserId = 1L
    )
    val userWithoutServerId = User(
        username = "User without server Id",
        role = "Admin",
        defaultUser = true,
        syncUser = true,
        userLevelDescription = "All privileges"
    )
    val tagWithoutServerId = Tag(
        tag = "Tag without serverId",
        syncTag = true
    )
}