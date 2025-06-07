package tech.bufallo.pw.scz.converter

enum class Encoding {
    IEEE754,
    INTERNAL,
    UNKNOWN;

    companion object {
        fun valueOfOrUnknown(name: String): Encoding {
            return try {
                valueOf(name.uppercase())
            } catch (e: IllegalArgumentException) {
                UNKNOWN
            }
        }
    }
}