package tech.bufallo.pw.scz.cmd

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.convert
import com.github.ajalt.clikt.parameters.arguments.validate
import tech.bufallo.pw.scz.converter.BiDirectConverter
import tech.bufallo.pw.scz.converter.StringToBinaryConverter.convertStringToUInt
import tech.bufallo.pw.scz.converter.StringToBinaryConverter.validateBinaryString
import tech.bufallo.pw.scz.converter.StringToBinaryConverter.validateHexString

class Ieee754ToInternalCmd : CliktCommand("from-ieee754-to-internal") {

    private val converter = BiDirectConverter()

    private val data: String by argument(
        "data",
        help = "Data to convert in hexadecimal notation (e.g. '477F FF00') or natual binary (e.g. '01000001010000100100001100001000') up to 32 bits"
    )
        .convert { it.replace(" ", "") }
        .convert { it.uppercase() }
        .validate {
            if (!validateBinaryString(data) && !validateHexString(data)) {
                fail(
                    """
                        |Input data is required, must be in hexadecimal notation (up to 8 hex digits) or natural binary (up to 32 bits) 
                        |e.g. '477FFF00' or '01000001010000100100001100001000'
                        |Current input: '$data'
                        |""".trimMargin()
                )
            }
        }

    override fun run() {
        println("Source data: ${data.uppercase()}")

        val inputValue = convertStringToUInt(data)
        val outputValue = converter.ieeeToInternal(inputValue)

        println("Converted data (hex): ${outputValue.toHexString().uppercase()}")
        println("Converted data (bin): ${outputValue.toString(2)}")
    }
}
