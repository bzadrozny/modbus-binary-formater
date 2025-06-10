package tech.bufallo.pw.scz.cmd

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.convert
import com.github.ajalt.clikt.parameters.arguments.validate
import tech.bufallo.pw.scz.converter.ConverterBiDirect

class InternalToIeee754Cmd : CliktCommand("from-internal-to-ieee754") {

    private val converter = ConverterBiDirect()

    private val data: String by argument("data", help = "Data to convert in hexadecimal notation (e.g. '477F FF00')")
        .convert { it.replace(" ", "") }
        .validate {
            if (
                it.isEmpty() ||
                it.length > 8 ||
                !it.all { char -> char.isDigit() || (char in 'A'..'F') || (char in 'a'..'f') }
            ) {
                fail("Input data is required, up to 4 bytes and must be in hexadecimal notation, e.g. '477FFF00'")
            }
        }

    override fun run() {
        println("Source data: 0x${data.uppercase()}")

        val inputValue = data
            .padStart(8, '0')
            .toUInt(16)

        val outputValue = converter.internalToIeee(inputValue)

        println("Converted data: 0x${outputValue.toHexString().uppercase()}")
    }
}
