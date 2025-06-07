package tech.bufallo.pw.scz.converter

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class ConverterBiDirectTest {

    private val converter = ConverterBiDirect()

    companion object {
        @JvmStatic
        fun testData() = listOf(
            // Non-floating point values test cases
            0x00000000U to 0x00000000U, // 0.0
            0x80000000U to 0x3F800000U, // 1.0
            0x81000000U to 0x40000000U, // 2.0
            0x83100000U to 0x41100000U, // 9.0
            0x8F7FFF00U to 0x477FFF00U, // 65535.0
            0x90000000U to 0x47800000U, // 65536.0
        )

        @JvmStatic
        fun randomTestData() = listOf(
            "00000000",
            "80000000",
            "81000000",
            "83100000",
            "8F7FFF00",
            "90000000",
            "80400000",
            "81100000",
            "83160000",
            "8F7FFF19",
            "90000000",
            "7F800000", // Infinity
            "7F800001", // NaN
            "FFFFFFFF", // All-bits set
            "7FFFFFFF", // Max positive value
            "80000001", // Min negative value
            "7F000000", // Max exponent
            "7F000001", // Max exponent with mantissa
        )
    }

    @ParameterizedTest(name = "{index} - {0} -> {1}")
    @MethodSource("testData")
    fun `test conversion from project data`(testCase: Pair<UInt, UInt>) {
        val (internal, ieee754) = testCase

        val resultIeee754 = converter.internalToIeee(internal)
        assertEquals(
            ieee754,
            resultIeee754,
            """
                |Conversion from internal to IEEE 754 failed for input: ${toBinaryString(internal)}
                |Expected: ${toBinaryString(ieee754)}
                |Actual: ${toBinaryString(resultIeee754)}
            """.trimMargin()
        )

        val resultInternal = converter.ieeeToInternal(ieee754)
        assertEquals(
            internal,
            resultInternal,
            """
                |Conversion from IEEE 754 to internal failed for input: ${toBinaryString(ieee754)}
                |Expected: ${toBinaryString(internal)}
                |Actual: ${toBinaryString(resultInternal)}
            """.trimMargin()
        )
    }

    @ParameterizedTest(name = "{index} - {0} -> {1}")
    @MethodSource("randomTestData")
    fun `test conversion from random data`(testCase: String) {
        // Input as internal representation
        val internal = testCase.toUInt(16)
        // Convert to IEEE 754 representation
        val ieee754FromInternal = converter.internalToIeee(internal)
        // Convert back to internal representation
        val internalBackFromIeee754 = converter.ieeeToInternal(ieee754FromInternal)
        assertEquals(
            internal, internalBackFromIeee754, """)
            |Conversion from internal to IEEE 754 and back failed for input: ${toBinaryString(internal)}
            |ieee754FromInternal: ${toBinaryString(ieee754FromInternal)}
            |internalBackFromIeee754: ${toBinaryString(internalBackFromIeee754)}
        """.trimMargin()
        )

        // Input as IEEE 754 representation
        val ieee754 = testCase.toUInt(16)
        // Convert to internal representation
        val internalFromIeee754 = converter.ieeeToInternal(ieee754)
        // Convert back to IEEE 754 representation
        val ieee754BackFromInternal = converter.internalToIeee(internalFromIeee754)
        assertEquals(
            ieee754, ieee754BackFromInternal, """)
            |Conversion from IEEE 754 to internal and back failed for input: ${toBinaryString(ieee754)}
            |internalFromIeee754: ${toBinaryString(internalFromIeee754)}
            |ieee754BackFromInternal: ${toBinaryString(ieee754BackFromInternal)}
        """.trimMargin()
        )
    }

    private fun toBinaryString(input: UInt): String {
        return input.toString(2).padStart(32, '0').chunked(4).joinToString(" ")
    }
}