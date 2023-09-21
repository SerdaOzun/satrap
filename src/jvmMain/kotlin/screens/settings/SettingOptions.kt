package screens.settings

import util.OS

/**
 * All Settings the user can configure in the Settingsmenu
 */
enum class SettingOptions(val label: String) {
    TERMINAL("Terminal"),
    SHELL("Shell")
}

enum class Terminal(val label: String, val os: OS) {
    CMD("cmd.exe", OS.WINDOWS),
    POWERSHELL("powershell.exe", OS.WINDOWS),
    TERMINAL("Terminal", OS.MACOS),
    ITERM("iTerm2", OS.MACOS),
    GNOME_TERMINAL("gnome-terminal", OS.LINUX);

    companion object {
        private val labelMap = entries.associateBy { it.label }

        fun byLabel(label: String?) = labelMap[label]
    }
}