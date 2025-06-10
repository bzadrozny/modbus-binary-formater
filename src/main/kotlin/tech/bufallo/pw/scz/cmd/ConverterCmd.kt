package tech.bufallo.pw.scz.cmd

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.convert
import com.github.ajalt.clikt.parameters.arguments.validate
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.validate
import tech.bufallo.pw.scz.converter.BiDirectConverter
import tech.bufallo.pw.scz.converter.Encoding
import tech.bufallo.pw.scz.converter.StringToBinaryConverter
import tech.bufallo.pw.scz.converter.StringToBinaryConverter.convertStringToUInt
import tech.bufallo.pw.scz.converter.StringToBinaryConverter.validateBinaryString
import tech.bufallo.pw.scz.converter.StringToBinaryConverter.validateHexString

class ConverterCmd : CliktCommand("modbus-binary-formater.jar") {

    private val converter = BiDirectConverter()

    private val data: String by argument("data", help = "Data to convert in hexadecimal notation (e.g. '477F FF00')")
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
        println("Source data: ${data.uppercase()}")

        if (intputEncoding == outputEncoding) {
            println("Input and output encodings are the same: ${intputEncoding.name}. No conversion needed.")
        }

        val inputValue = convertStringToUInt(data)

        val outputValue = when (intputEncoding) {
            Encoding.INTERNAL -> converter.internalToIeee(inputValue)
            Encoding.IEEE754 -> converter.ieeeToInternal(inputValue)
            Encoding.UNKNOWN -> throw IllegalArgumentException("Unsupported input encoding format: $intputEncoding")
        }

        println("Converted data (hex): ${outputValue.toHexString().uppercase()}")
        println("Converted data (bin): ${outputValue.toString(2)}")
    }
}
