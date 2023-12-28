package util

enum class ErrorCodes(val code: Int, val message: String = "An Error occurred. Please file an issue. Code #") {
    SERVER_ID_NULL(100),
    TAG_ID_NULL(101),
}