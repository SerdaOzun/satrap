package util

val os: OS = run {
    val os = System.getProperty("os.name").lowercase()

    when {
        os.contains("win") -> OS.WINDOWS
        os.contains("nix") || os.contains("nux") || os.contains("mac") -> {
            if (os.contains("mac")) {
                OS.MACOS
            } else {
                OS.LINUX
            }
        }

        else -> OS.UNKNOWN
    }
}

enum class OS {
    WINDOWS, MACOS, LINUX, UNKNOWN
}
