import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import data.*
import domain.ServerDataSource
import domain.SettingsDataSource
import domain.TagDataSource
import domain.UserDataSource
import org.testng.annotations.BeforeClass
import satrapco.satrap.Database


open class DatabaseTestCase {
    lateinit var serverDataSource: ServerDataSource
    lateinit var serverCompleteDataSource: SqlDelightServerComplete
    lateinit var tagDataSource: TagDataSource
    lateinit var userDataSource: UserDataSource
    lateinit var settingsDataSource: SettingsDataSource

    @BeforeClass
    fun createDatabase() {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).apply { Database.Schema.create(this) }
        val db = Database(driver)
        serverDataSource = SqlDelightServer(db)
        serverCompleteDataSource = SqlDelightServerComplete(db)
        tagDataSource = SqlDelightTag(db)
        userDataSource = SqlDelightUser(db)
        settingsDataSource = SqlDelightSettings(db)
    }

}