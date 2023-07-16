import app.cash.sqldelight.db.SqlDriver

// in src/commonMain/kotlin
expect class DriverFactory {
    fun createDriver(): SqlDriver
}