package settings

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
import util.os

class Config(
    private val settingsDataSource: SettingsDataSource = AppDatabase.sqlDelightSettings
) : ViewModel() {

    var changesMade by mutableStateOf(false)

    var terminal by mutableStateOf(Terminal.NONE)
    var shell by mutableStateOf("")

    fun onEvent(configEvent: ConfigEvent) = when (configEvent) {
        ConfigEvent.Reset -> resetToDefaultSettings()
        ConfigEvent.Save -> saveTerminalAndShell()
    }

    init {
        //Have the settings already been initialized once?
        var settingsInitialized: Boolean = true

        viewModelScope.launch {
            settingsInitialized = settingsDataSource.getAllSettings().isNotEmpty()
        }

        //Save default settings to database
        if (!settingsInitialized) {
            resetToDefaultSettings()
        }

        getSettingsFromDB()
    }

    private fun getDefaultTerminal(): Terminal = when (os) {
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
            if (os == OS.LINUX) {
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
    object Reset : ConfigEvent()
    object Save : ConfigEvent()
}