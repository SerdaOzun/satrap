package screens.settings

import AppDatabase
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import domain.Setting
import domain.SettingsDataSource
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import util.OS
import util.currentOS

/**
 * Read and write all available Setings from and to the database.
 * @see SettingOptions for available settings which are also top level Variables in this class
 */
class Config(
    private val settingsDataSource: SettingsDataSource = AppDatabase.sqlDelightSettings
) : ViewModel() {

    var changesMade by mutableStateOf(false)

    //All Settings available
    var terminal by mutableStateOf(Terminal.entries.first { it.os == currentOS })
    var shell by mutableStateOf("")

    fun onEvent(configEvent: ConfigEvent) = when (configEvent) {
        ConfigEvent.Reset -> resetToDefaultSettings()
        ConfigEvent.Save -> saveTerminalAndShell()
    }

    init {
        //Have the settings already been initialized once?
        var settingsInitialized = true
        viewModelScope.launch {
            settingsInitialized = settingsDataSource.getAllSettings().isNotEmpty()
        }

        //Save default settings to database
        if (!settingsInitialized) {
            resetToDefaultSettings()
        }

        getSettingsFromDB()
    }

    private fun getDefaultTerminal(): Terminal = when (currentOS) {
        OS.WINDOWS -> Terminal.CMD
        OS.MACOS -> Terminal.TERMINAL
        else -> Terminal.GNOME_TERMINAL
    }

    private fun resetToDefaultSettings() {
        viewModelScope.launch {
            settingsDataSource.insertSetting(Setting(SettingOptions.TERMINAL.label, getDefaultTerminal().label))
            settingsDataSource.insertSetting(Setting(SettingOptions.SHELL.label, "bash"))
        }

        changesMade = false

        getSettingsFromDB()
    }

    private fun saveTerminalAndShell() {
        viewModelScope.launch {
            settingsDataSource.insertSetting(Setting(SettingOptions.TERMINAL.label, terminal.label))
            if (currentOS == OS.LINUX) {
                settingsDataSource.insertSetting(Setting(SettingOptions.SHELL.label, shell))
            }
        }

        changesMade = false

        getSettingsFromDB()
    }

    private fun getSettingsFromDB() {
        viewModelScope.launch {
            terminal = Terminal.byLabel(settingsDataSource.getSetting(SettingOptions.TERMINAL.label)?.value) ?: getDefaultTerminal()
            shell = settingsDataSource.getSetting(SettingOptions.SHELL.label)?.value ?: "bash"
        }
    }
}

sealed class ConfigEvent {
    data object Reset : ConfigEvent()
    data object Save : ConfigEvent()
}