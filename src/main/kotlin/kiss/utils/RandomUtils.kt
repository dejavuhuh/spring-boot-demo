package kiss.utils

object RandomUtils {

    val candidate = 0..9

    fun number(length: Int): String {
        return (1..length)
            .map { candidate.random() }
            .joinToString("")
    }
}