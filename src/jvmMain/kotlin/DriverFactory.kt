import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import satrapin.satrap.Database
import util.OS
import util.os
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:${getDatabaseDirectory()}")
        Database.Schema.create(driver)
        return driver
    }
}


/**
 * Creates the directory in which the Database will be saved on the User's system
 */
private fun getDatabaseDirectory(): Path {

    val path = when (os) {
        OS.WINDOWS -> {
            Paths.get(System.getenv("APPDATA"), "satrap")
        }

        OS.MACOS -> {
            Paths.get(System.getProperty("user.home"), "Library", "Application Support", "satrap")
        }

        OS.LINUX -> {
            Paths.get(System.getProperty("user.home"), ".config", "satrap")
        }

        else -> {
            throw UnsupportedOperationException("Unsupported operating system: $os")
        }
    }

    if(!Files.exists(path)) {
        Files.createDirectories(path)
    }

    return Paths.get(path.toString(), "satrap.sqlite")
}