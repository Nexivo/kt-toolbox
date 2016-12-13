package org.nexivo.kt.toolbox.io.dsl

import org.nexivo.kt.specifics.collections.forEach
import org.nexivo.kt.toolbox.collections.IterableFacade
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream

infix fun InputStream.`while`(predicate: (Int) -> Boolean): Iterable<Int> = IterableFacade(this, InputStream::read, predicate)

infix fun InputStream.until(predicate: (Int) -> Boolean): Iterable<Int> = IterableFacade(this, InputStream::read, { !predicate(it) })

infix fun InputStream.until(signal: Int): Iterable<Int> = IterableFacade(this, InputStream::read, signal)

fun InputStream.capture(): OutputStream
    = ByteArrayOutputStream().use {
        capture ->

        BufferedInputStream(this) until -1 forEach { capture.write(it) }

        capture
    }
