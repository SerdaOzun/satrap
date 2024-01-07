package data

import domain.Setting
import domain.SettingsDataSource
import domain.toSetting
import domain.toSettingsEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import satrapco.satrap.Database

class SqlDelightSettings(
    db: Database,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : SettingsDataSource {
    private val queries = db.settingsQueries

    override fun insertSetting(setting: Setting) {
         queries.insertSetting(setting.toSettingsEntity())
    }

    override fun getSetting(setting: String): Setting? {
        return queries.getSetting(setting).executeAsOneOrNull()?.toSetting()

    }

    override fun getAllSettings(): List<Setting> {
        return queries.getAllSettings().executeAsList().map { it.toSetting() }
    }
}