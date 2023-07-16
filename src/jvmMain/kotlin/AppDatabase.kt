import data.SqlDelightServer
import data.SqlDelightSettings
import data.SqlDelightTag
import data.SqlDelightUser

object AppDatabase {
    private val database = createDatabase(DriverFactory())

    val sqlDelightServer = SqlDelightServer(database)
    val sqlDelightTag = SqlDelightTag(database)
    val sqlDelightUser = SqlDelightUser(database)

    val sqlDelightSettings = SqlDelightSettings(database)
}