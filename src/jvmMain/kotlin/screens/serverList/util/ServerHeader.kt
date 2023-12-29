package screens.serverList.util

enum class ServerHeader(val label: String, val weight: Float, val headerWeight: Float = weight) {
    RUN("", 0.04f ),
    COPY("", weight = 0.04f),
    SERVER("Server", 0.22f, headerWeight = 0.3f),
    USER("User", 0.2f),
    DESCRIPTION("Description", 0.5f);
}