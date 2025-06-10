package tech.bufallo.pw.scz

import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands
import tech.bufallo.pw.scz.cmd.ConverterCmd
import tech.bufallo.pw.scz.cmd.Ieee754ToInternalCmd
import tech.bufallo.pw.scz.cmd.InternalToIeee754Cmd
import tech.bufallo.pw.scz.cmd.ToolCmd

fun main(args: Array<String>) = ToolCmd()
    .subcommands(Ieee754ToInternalCmd(), InternalToIeee754Cmd(), ConverterCmd())
    .main(args)
