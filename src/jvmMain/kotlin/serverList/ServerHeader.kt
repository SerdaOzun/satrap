package serverList

enum class ServerHeader(val label: String, val weight: Float) {
    RUN("", 0.05f),
    SERVER("Server", 0.25f),
    USER("User", 0.2f),
    DESCRIPTION("Description", 0.5f);
}