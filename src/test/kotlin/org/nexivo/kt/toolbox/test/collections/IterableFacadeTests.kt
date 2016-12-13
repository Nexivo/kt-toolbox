package org.nexivo.kt.toolbox.test.collections

import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.nexivo.kt.toolbox.collections.IterableFacade
import org.nexivo.kt.toolbox.test.mockedStream
import org.nexivo.kt.toolbox.test.throws
import java.io.InputStream

class IterableFacadeTests: Spek({

    describe("IterableFacade behavior") {

        given("an Object which provides a series of data") {

            given("a simple end signal indicator") {

                it("should be able to create an Iterable<T> facade") {
                    val subject: IterableFacade<InputStream, Int> = IterableFacade("Jack\n".mockedStream(), InputStream::read, CARRIAGE_RETURN)
                    val actual:   String                           = subject.map(Int::toChar).joinToString(",")
                    val expected: String                           = "J,a,c,k"

                    actual.should.be.equal(expected)
                }
            }

            given("custom end signaling logic") {

                val endSignal: () -> (Int) -> Boolean = {
                    var last: Int = -1

                    {
                        signal ->

                        if (signal == CARRIAGE_RETURN && last == CARRIAGE_RETURN) {
                            false
                        } else {
                            last = signal

                            true
                        }
                    }
                }

                it ("should be able to create an Iterable<T> facade") {
                    val subject: IterableFacade<InputStream, Int> = IterableFacade("Jack\nAnd\nJill\n\n".mockedStream(), InputStream::read, endSignal())
                    val actual:   String                           = subject.map({ if (it != CARRIAGE_RETURN) it.toChar() else '\\' }).joinToString(",")
                    val expected: String                           = "J,a,c,k,\\,A,n,d,\\,J,i,l,l,\\"

                    actual.should.be.equal(expected)
                }
            }

            on("calling \"next\" past the end") {

                it("should throw \"IndexOutOfBoundsException\"") {
                    val subject: Iterator<Int> = IterableFacade("Foo\n".mockedStream(), InputStream::read, CARRIAGE_RETURN).iterator()

                    while (subject.hasNext()) {
                        subject.next()
                    }

                    throws<IndexOutOfBoundsException> {
                        subject.next()
                    }
                }
            }

            on("calling \"hasNext\" more than once") {

                it("should not advance the iterator") {
                    val subject: Iterator<Int> = IterableFacade("Foo\n".mockedStream(), InputStream::read).iterator()

                    subject.hasNext()

                    subject.next()

                    subject.hasNext()
                    subject.hasNext()

                    val actual:   Char = subject.next().toChar()
                    val expected: Char = 'o'

                    actual.should.be.equal(expected)
                }
            }

            on("calling \"next\" without calling \"hasNext\" before hand") {

                it("should throw \"IndexOutOfBoundsException\"") {
                    val subject: Iterator<Int> = IterableFacade("Foo\n".mockedStream(), InputStream::read).iterator()

                    throws<IndexOutOfBoundsException> {
                        subject.next()
                    }
                }
            }

            on("calling \"next\" consecutively without calling \"hasNext\" for each call") {

                it("should throw \"IndexOutOfBoundsException\"") {
                    val subject: Iterator<Int> = IterableFacade("Foo\n".mockedStream(), InputStream::read).iterator()

                    subject.hasNext()

                    subject.next()

                    throws<IndexOutOfBoundsException> {
                        subject.next()
                    }
                }
            }
        }
    }
}) {
    companion object {

        private const val CARRIAGE_RETURN: Int = '\n'.toInt()
    }
}