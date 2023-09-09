import satrapco.satrap.Database
fun createDatabase(driverFactory: DriverFactory): Database {
    val driver = driverFactory.createDriver()

    return Database(driver)
}