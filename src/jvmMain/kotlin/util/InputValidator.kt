package util

object InputValidator {

    fun getDefaultErrorIdOrNull(input: String): Int? {
        return when {
            input.isEmpty() -> InputErrorMessages.VALUE_EMPTY.errorId
            else -> null
        }
    }

}

data class InputWrapper(
    val value: String = "",
    val errorId: Int? = null
)

enum class InputErrorMessages(val errorId: Int, val errorMessage: String) {
    VALUE_EMPTY(1, "Value can not be empty"),
    VALUE_TOO_LONG(2, "The value is too long");

    companion object {
        fun byErrorId(id: Int): String = values().first { it.errorId == id }.errorMessage
    }
}