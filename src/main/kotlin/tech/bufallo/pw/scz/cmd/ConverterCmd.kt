package tech.bufallo.pw.scz.cmd

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.convert
import com.github.ajalt.clikt.parameters.arguments.validate
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.validate
import tech.bufallo.pw.scz.converter.ConverterBiDirect
import tech.bufallo.pw.scz.converter.Encoding

class ConverterCmd : CliktCommand(name = "convert") {

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

    private val intputEncoding: Encoding by option("--input-encoding", "-i", help = "Source encoding format: [${Encoding.INTERNAL}, ${Encoding.IEEE754}]")
        .convert { Encoding.valueOfOrUnknown(it.uppercase()) }
        .default(Encoding.UNKNOWN)
        .validate {
            if (it == Encoding.UNKNOWN) {
                fail("Input encoding is required - format: [${Encoding.INTERNAL}, ${Encoding.IEEE754}], example usage: -i IEEE754")
            }
        }

    private val outputEncoding: Encoding by option("--output-encoding", "-o", help = "Output encoding format: [${Encoding.INTERNAL}, ${Encoding.IEEE754}]")
        .convert { Encoding.valueOfOrUnknown(it.uppercase()) }
        .default(Encoding.UNKNOWN)
        .validate {
            if (it == Encoding.UNKNOWN) {
                fail("Output encoding is required - format: [${Encoding.INTERNAL}, ${Encoding.IEEE754}], example usage: -o INTERNAL")
            }
        }

    override fun run() {
        println("Source data: 0x${data.uppercase()}")

        if (intputEncoding == outputEncoding) {
            println("Input and output encodings are the same: ${intputEncoding.name}. No conversion needed.")
        }

        val inputValue = data
            .padStart(8, '0')
            .toUInt(16)

        val outputValue = when (intputEncoding) {
            Encoding.INTERNAL -> converter.internalToIeee(inputValue)
            Encoding.IEEE754 -> converter.ieeeToInternal(inputValue)
            Encoding.UNKNOWN -> throw IllegalArgumentException("Unsupported input encoding format: $intputEncoding")
        }
        
        println("Converted data: 0x${outputValue.toHexString().uppercase()}")
    }
}
