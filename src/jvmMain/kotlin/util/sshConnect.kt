package util

import config
import screens.settings.Terminal
import java.io.File


fun runCommand(command: String) {
    when (currentOS) {
        OS.WINDOWS -> executeOnWindows(command)
        OS.MACOS -> executeOnMac(command)
        else -> executeOnUbuntu(command)
    }
}

private fun executeOnWindows(command: String) {
    when (config.terminal) {
        Terminal.CMD -> {
            val requestArray = "cmd.exe /c start cmd.exe /k $command".split(" ").toTypedArray()
            Runtime.getRuntime().exec(requestArray)
        }

        Terminal.POWERSHELL -> {
            val builder = ProcessBuilder("cmd.exe", "/c", "start", "powershell.exe", "-NoExit", "-Command", command)
            val process = builder.inheritIO().start()
            process.waitFor()
        }

        else -> {}
    }

}

private fun executeOnMac(command: String) {
    val script = when (config.terminal) {
        Terminal.TERMINAL -> {
            """
            tell application "${config.terminal}"
                do script "$command"
               activate
            end tell
            """.trimIndent()
        }

        Terminal.ITERM -> {
            """
            tell application "${config.terminal}"
               set newWindow to (create window with profile command "$command")
            end tell
            """.trimIndent()
        }

        else -> {
            ""
        }
    }

    val tempFile = File.createTempFile("script", ".scpt")
    tempFile.writeText(script)

    val process = Runtime.getRuntime().exec(arrayOf("osascript", tempFile.absolutePath))
    process.waitFor()
    tempFile.delete()
}

private fun executeOnUbuntu(command: String) {
    val script = """
        ${config.terminal} -- /bin/${config.shell} -c "$command; exec ${config.shell}"
    """.trimIndent()

    val tempFile = File.createTempFile("script", ".sh")
    tempFile.writeText(script)

    val process = ProcessBuilder("bash", tempFile.absolutePath).start()
    process.waitFor()
    tempFile.delete()
}
