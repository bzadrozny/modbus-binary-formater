package tech.bufallo.pw.scz.converter

class ConverterBiDirect {

    private val zeroMask: UInt = 0x00000000U
    private val oneMask: UInt = 0xFFFFFFFFU

    private val signMask: UInt = 0x80000000U

    private val ieeeExponentMask: UInt = 0x7F800000U
    private val internalExponentMask: UInt = 0x7F000000U

    private val mantisMask: UInt = 0x7FFFFFU

    private val internalMantisNegativeMask = 0x00800000U
    private val internalMantisPositiveMask = zeroMask

    fun ieeeToInternal(ieee: UInt): UInt {
        if (ieee == zeroMask) return zeroMask

        val ieeeSign = (ieee and signMask)

        val ieeeExponent = (ieee and ieeeExponentMask) shr 23
        var realExponentIsNegative = ieeeExponent < 127U
        var realExponent = if (realExponentIsNegative) ieeeExponent xor 127U else ieeeExponent - 127U

        // Special case for MIN/MAX IEEE value
        if (realExponent == 128U) {
            realExponentIsNegative = true
            realExponent = 0U
        }

        val ieeeMantissa = ieee and mantisMask

        return (signMask xor ieeeSign) or
                (internalExponentMask and (realExponent shl 24)) or
                (if (realExponentIsNegative) internalMantisNegativeMask else internalMantisPositiveMask) or
                (mantisMask and ieeeMantissa) and
                oneMask
    }

    fun internalToIeee(internal: UInt): UInt {
        if (internal == zeroMask) return zeroMask

        val internalSign = (internal and signMask)

        var realExponentIsNegative = (internal and internalMantisNegativeMask) shr 23 == 1U
        var internalExponent = (internal and internalExponentMask) shr 24

        // Special case for MIN/MAX IEEE value
        if (internalExponent == 0U && realExponentIsNegative) {
            realExponentIsNegative = false
            internalExponent = 128U
        }

        val ieeeExponent = if (realExponentIsNegative) internalExponent xor 127U else internalExponent + 127U

        val ieeeMantissa = internal and mantisMask

        return (signMask xor internalSign) or
                (ieeeExponentMask and (ieeeExponent shl 23)) or
                (mantisMask and ieeeMantissa) and
                oneMask
    }
}