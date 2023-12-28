package screens.serverList.util

enum class ServerHeader(val label: String, val weight: Float, val headerWeight: Float = weight) {
    RUN("", 0.05f ),
    SERVER("Server", 0.25f, headerWeight = 0.3f),
    USER("User", 0.2f),
    DESCRIPTION("Description", 0.5f);
}