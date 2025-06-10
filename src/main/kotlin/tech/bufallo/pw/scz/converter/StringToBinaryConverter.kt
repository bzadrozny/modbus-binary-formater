package tech.bufallo.pw.scz.converter

object StringToBinaryConverter {

    val binaryPattern = "^[01]{1,32}$".toRegex()
    val hexPattern = "^[0-9A-Fa-f]{1,8}$".toRegex()

    fun validateBinaryString(binaryString: String): Boolean {
        return binaryPattern.matches(binaryString.replace(" ", ""))
    }

    fun validateHexString(hexString: String): Boolean {
        return hexPattern.matches(hexString.replace(" ", ""))
    }

    fun convertStringToUInt(data: String): UInt = if (validateBinaryString(data)) {
        convertBinaryStringToUInt(data)
    } else if (validateHexString(data)) {
        convertHexStringToUInt(data)
    } else {
        throw IllegalArgumentException("Input data is invalid or unsupported format. Must be in hexadecimal notation (up to 8 hex digits) or natural binary (up to 32 bits).")
    }

    private fun convertBinaryStringToUInt(binaryString: String): UInt {
        return binaryString
            .replace(" ", "")
            .padStart(32, '0')
            .toUInt(2)
    }

    private fun convertHexStringToUInt(hexString: String): UInt {
        return hexString
            .replace(" ", "")
            .padStart(8, '0')
            .toUInt(16)
    }
}