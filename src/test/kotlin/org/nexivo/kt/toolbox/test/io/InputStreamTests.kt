package org.nexivo.kt.toolbox.test.io

import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.nexivo.kt.toolbox.io.dsl.`while`
import org.nexivo.kt.toolbox.io.dsl.capture
import org.nexivo.kt.toolbox.io.dsl.until
import org.nexivo.kt.toolbox.test.mockedStream
import java.io.InputStream
import java.io.OutputStream

class InputStreamTests: Spek({

    describe("InputStream \"capture\" behavior") {

        on("calling the \"capture\" method with an InputStream") {

            it("should return an OutputStream with the input data") {

                val inputStream: InputStream  = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.\nAliquam malesuada ipsum consectetur dolor dignissim, in.\n".mockedStream()
                val actual:      OutputStream = inputStream.capture()
                val expected:    String       = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.\nAliquam malesuada ipsum consectetur dolor dignissim, in.\n"

                actual.toString().should.be.equal(expected)
            }
        }
    }

    describe("InputStream \"until\" behavior") {

        given("an InputStream") {

            given("that needs end signaling logic") {

                it("should create an Iterable<T> facade") {

                    val subject:  Iterable<Int> = ("A stream of consciousness ...≈disregarded superfluous content".mockedStream() until { it == END_SIGNAL })
                    val actual:   String        = subject.map(Int::toChar).joinToString("")
                    val expected: String        = "A stream of consciousness ..."

                    actual.should.be.equal(expected)
                }
            }

            given("it has a simple end signal indicator") {

                it("should create an Iterable<T> facade") {

                    val subject:  Iterable<Int> = "A simpler stream of consciousness ...≈disregarded superfluous content".mockedStream() until END_SIGNAL
                    val actual:   String        = subject.map(Int::toChar).joinToString("")
                    val expected: String        = "A simpler stream of consciousness ..."

                    actual.should.be.equal(expected)
                }
            }
        }
    }
    describe("InputStream \"while\" behavior") {

        given("an InputStream") {

            it("should create an Iterable<T> facade") {

                val subject:  Iterable<Int> = ("A stream of consciousness ...≈disregarded superfluous content".mockedStream() `while` { it != END_SIGNAL })
                val actual:   String        = subject.map(Int::toChar).joinToString("")
                val expected: String        = "A stream of consciousness ..."

                actual.should.be.equal(expected)
            }
        }
    }
}) {

    companion object {

        private const val END_SIGNAL: Int = '≈'.toInt()
    }
}
