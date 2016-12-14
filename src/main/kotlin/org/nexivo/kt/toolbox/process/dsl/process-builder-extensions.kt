package org.nexivo.kt.toolbox.process.dsl

import org.nexivo.kt.specifics.flow.ifSet
import org.nexivo.kt.specifics.flow.ifValid
import org.nexivo.kt.toolbox.io.dsl.capture
import java.io.File

fun Array<String>.start() = this.start(null as File?, null as File?, null as File?)

fun Array<String>.start(vararg variables: Pair<String, String>) = this.start(null as File?, null as File?, null as File?, *variables)

fun Array<String>.start(
        input:  String?,
        output: String?,
        errors: String?,
        vararg variables: Pair<String, String>
    ): Process = this.start(input ifValid ::File, output ifValid ::File, errors ifValid ::File, *variables)

fun Array<String>.start(
        input:  File?,
        output: File?,
        errors: File?,
        vararg variables: Pair<String, String>
    ): Process {

    val process = ProcessBuilder(*this)

    input ifSet { process.redirectError(input) }

    output ifSet { process.redirectError(output) }

    errors ifSet { process.redirectError(errors) }

    process.environment().putAll(variables)

    return process.start()
}

fun Array<String>.startAndCapture(): Pair<Int, String>
    = this.startAndCapture(null as File?, null as File?, null as File?)

fun Array<String>.startAndCapture(vararg variables: Pair<String, String>): Pair<Int, String>
    = this.startAndCapture(null as File?, null as File?, null as File?, *variables)

fun Array<String>.startAndCapture(
        input:  String?,
        output: String?,
        errors: String?,
        vararg variables: Pair<String, String>
): Pair<Int, String>
    = this.startAndCapture(input ifValid ::File, output ifValid ::File, errors ifValid ::File, *variables)

fun Array<String>.startAndCapture(
        input:  File? = null,
        output: File? = null,
        errors: File? = null,
        vararg variables: Pair<String, String>
    ): Pair<Int, String> {

    val process:  Process = this.start(input, output, errors, *variables)
    val captured: String  = process.inputStream.capture().toString()

    return process.exitValue() to captured
}
