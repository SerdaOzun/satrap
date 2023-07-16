package domain

import satrapinsatrap.SettingsEntity

interface SettingsDataSource {
    suspend fun insertSetting(setting: Setting)
    suspend fun getSetting(setting: String): Setting?
    suspend fun getAllSettings(): List<Setting>
}

data class Setting(
    val setting: String,
    var value: String
)

fun SettingsEntity.toSetting() = Setting(
    setting, value_
)

fun Setting.toSettingsEntity() = SettingsEntity(setting, value)