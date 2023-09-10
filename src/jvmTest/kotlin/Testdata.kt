import domain.Server
import domain.Tag
import domain.User

object Testdata {
    val server = Server(
        serverId = null,
        serverUrl = "my-server-url",
        title = "server-title",
        organization = "Testorg",
        description = "test",
        syncServer = false
    )
    val userWithoutServerId = User(
        null,
        null,
        username = "User without server Id",
        role = "Admin",
        defaultUser = true,
        syncUser = true,
        userLevelDescription = "All privileges"
    )
    val tagWithoutServerId = Tag(
        tagId = null,
        serverId = null,
        tag = "Tag without serverId",
        syncTag = true
    )
}