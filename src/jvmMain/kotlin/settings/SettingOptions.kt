package settings

import util.OS

/**
 * All Settings the user can configure in the Settingsmenu
 */
enum class SettingOptions(val label: String) {
    TERMINAL("Terminal"),
    SHELL("Shell")
}

enum class Terminal(val label: String, val os: OS) {
    NONE("", OS.UNKNOWN),
    CMD("cmd.exe", OS.WINDOWS),
    POWERSHELL("powershell.exe", OS.WINDOWS),
    TERMINAL("Terminal", OS.MACOS),
    ITERM("iTerm2", OS.MACOS),
    GNOME_TERMINAL("gnome-terminal", OS.LINUX);

    companion object {
        fun byLabel(label: String?) = entries.first { label == it.label }
    }
}