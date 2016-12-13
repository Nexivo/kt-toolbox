package org.nexivo.kt.toolbox.test

import mockit.Mock
import mockit.MockUp
import java.io.InputStream

inline fun <reified T : Any> className() = T::class.java.simpleName!!

inline fun <reified T> throws(block: () -> Unit)
        where T : Throwable
{
    try {
        block()
    } catch (ex: Throwable) {

        if (ex is T) { return@throws }

        throw ex
    }

    throw AssertionError("Expected ${className<T>()} was NOT Thrown!")
}

fun String.mockedStream (): InputStream = mockedStream(*this.map(Char::toInt).toIntArray())

fun mockedStream (vararg values: Int): InputStream =

    object : MockUp<InputStream>() {

        val iterated: Iterator<Int> = values.iterator()
        var complete: Boolean       = false

        @Mock fun read(): Int = if (iterated.hasNext()) { iterated.next() } else { -1 }

        @Mock fun read(b: ByteArray, off: Int, len: Int): Int =
            if (complete) {
                -1
            } else {

                val end = off + len

                (off..end).forEachIndexed { idx, value ->
                    if(value < values.size) {
                        b[idx] = values[value].toByte()
                    }
                }

                complete = end >= values.size

                values.size
            }
    }.mockInstance
