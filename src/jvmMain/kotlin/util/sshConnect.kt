package util

import config
import java.io.File


fun runCommand(command: String) {
    when (os) {
        OS.WINDOWS -> executeOnWindows(command)
        OS.MACOS -> executeOnMac(command)
        else -> executeOnUbuntu(command)
    }
}

private fun executeOnWindows(command: String) {
    val requestArray = "${config.terminal} /c start ${config.terminal} /k $command".split(" ").toTypedArray()
    Runtime.getRuntime().exec(requestArray)
}

private fun executeOnMac(command: String) {
    val script = """
    tell application "${config.terminal}"
        do script "$command"
        activate
    end tell
""".trimIndent()

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
