package tech.bufallo.pw.scz.converter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class StringToBinaryConverterTest {

    companion object {
        @JvmStatic
        fun validTestData() = listOf(
            "A" to "1010",
            "0" to "0",
            "00000000" to "00000000000000000000000000000000",
            "80000000" to "10000000000000000000000000000000",
            "81000 000" to "10000001000000000000000000000000",
            "83100000" to "10000011000100000000000000000000",
            "8F7FFF00" to "1000111101111111111111 1100000000",
            "90000000" to "10010000000000000000000000000000",
            "80400000" to "10000000010000000000000000000000",
            "81100000" to "10000001000100000000000000000000",
            "83160000" to "10000011000101100000000000000000",
            "8F7FFF19" to "10001111011111111111111100011001",
            "41100000" to "0100000100010000000 0000000000000",
            "477FFF00" to "01000111011111111111111100000000",
            "90000000" to "10010000000000000000000000000000",
            "7F8 00000" to "01111111100 000000000000000000000", // Infinity
            "7F800001" to "01111111100000000000000000000001", // NaN
            "FFFFFFFF" to "11111111111111111111111111111111", // All-bits set
            "7FFF FFFF" to "011111111 11111111111111111111111", // Max positive value
            "80000001" to "10000000000000000000000000000001", // Min negative value
            "7F000000" to "01111111000000000000000000000000", // Max exponent
            "7 F000 0 01" to "0 111 1111000 0000000 00000000000001", // Max exponent with mantissa
        )

        @JvmStatic
        fun invalidTestData() = listOf(
            "00000000G" to "124Z",
            "800000001" to "10000000000190000000000000000000000",
            "8100000Z" to "10000005000000000000000000000000",
            "-83100000" to "10000011s",
            ".8F7FFF00" to "100011110AW11111111111111100000000",
        )
    }

    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource("validTestData")
    fun `test conversion from string to binary`(testCase: Pair<String, String>) {
        val (hex, bin) = testCase

        assertEquals(true, StringToBinaryConverter.validateBinaryString(bin), "Invalid hex string: $hex")
        assertEquals(true, StringToBinaryConverter.validateHexString(hex), "Invalid hex string: $hex")

        val binUInt = StringToBinaryConverter.convertStringToUInt(bin)
        val hexUInt = StringToBinaryConverter.convertStringToUInt(hex)

        assertEquals(
            binUInt, hexUInt, """
                |Conversion from string to UInt failed for input: $testCase
                |Binary: ${binUInt.toString(2).padStart(32, '0')}
                |Hex: ${hexUInt.toString(16).padStart(8, '0').uppercase()}
            """.trimMargin()
        )

        assertEquals(
            bin.replace(" ", "").padStart(32, '0'),
            binUInt.toString(2).padStart(32, '0'),
            """
                |Binary conversion mismatch for input: $testCase
                |Expected: ${bin.padStart(32, '0')}
                |Actual: ${binUInt.toString(2).padStart(32, '0')}
            """.trimMargin()
        )

        assertEquals(
            hex.replace(" ", "").padStart(8, '0'),
            hexUInt.toString(16).padStart(8, '0').uppercase(),
            """
                |Hex conversion mismatch for input: $testCase
                |Expected: ${hex.padStart(8, '0').uppercase()}
                |Actual: ${hexUInt.toString(16).padStart(8, '0').uppercase()}
            """.trimMargin()
        )
    }

    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource("invalidTestData")
    fun `test invalid conversion from string to binary`(testCase: Pair<String, String>) {
        val (hex, bin) = testCase

        assertThrows<IllegalArgumentException> {
            StringToBinaryConverter.convertStringToUInt(hex)
        }

        assertThrows<IllegalArgumentException> {
            StringToBinaryConverter.convertStringToUInt(bin)
        }
    }
}