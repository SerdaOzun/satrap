import data.*

/**
 * Contains the necessary objects with which to interact with the database
 */
object AppDatabase {
    private val database = createDatabase(DriverFactory())

    val sqlServer = SqlDelightServer(database)
    val sqlServerComplete = SqlDelightServerComplete(database)
    val sqlTag = SqlDelightTag(database)
    val sqlUser = SqlDelightUser(database)

    val sqlSettings = SqlDelightSettings(database)
}