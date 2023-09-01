package domain

import satrapinsatrap.SettingsEntity

/**
 * Allows to write and retrieve Settings which the User may configure within the Application
 * @see Setting
 */
interface SettingsDataSource {
    suspend fun insertSetting(setting: Setting)
    suspend fun getSetting(setting: String): Setting?
    suspend fun getAllSettings(): List<Setting>
}

/**
 * @param setting the name of the setting
 * @param value
 */
data class Setting(
    val setting: String,
    var value: String
)

fun SettingsEntity.toSetting() = Setting(
    setting, value_
)

fun Setting.toSettingsEntity() = SettingsEntity(setting, value)