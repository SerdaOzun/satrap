package settings

import util.OS

enum class SettingOptions(val label: String) {
    TERMINAL("Terminal"),
    SHELL("Shell")
}

enum class Terminal(val label: String, val os: OS) {
    NONE("", OS.UNKNOWN),
    CMD("cmd.exe", OS.WINDOWS),
    POWERSHELL("powershell.exe", OS.WINDOWS),
    TERMINAL("Terminal", OS.MACOS),
    GNOME_TERMINAL("gnome-terminal", OS.LINUX);

    companion object {
        fun byLabel(label: String?) = values().first { label == it.label }
    }
}