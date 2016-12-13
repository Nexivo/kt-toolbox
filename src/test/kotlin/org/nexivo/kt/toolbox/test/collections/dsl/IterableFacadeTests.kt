package org.nexivo.kt.toolbox.test.collections.dsl

import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.nexivo.kt.toolbox.collections.dsl.iterateUntil
import org.nexivo.kt.toolbox.collections.dsl.iterateWhile
import java.util.*
import kotlin.reflect.KFunction1

class IterableFacadeTests : Spek({

    describe("\"iterateUntil\" extension behavior") {

        given("any object that produces a collection of <T> values") {

            given("that needs end signaling logic") {

                it("should be treated as an Iterable<T> until condition is true") {

                    val subject: Iterable<Int> = SERIES_OF_NUMBERS.iterateUntil(NEXT_FUNCTION) { it == LAST_RECORD_INDICATOR }
                    val actual:  List<Int>     = subject.toList()
                    val expected: Array<Int>   = arrayOf(1, 2, 3, 4)

                    actual.should.contain.all.elements(*expected)
                    actual.should.have.a.size.be.equal(expected.size)
                }
            }

            given("it has a simple end signal indicator") {

                it("should be treated as an Iterable<T> until condition is true") {

                    val subject: Iterable<Int> = SERIES_OF_NUMBERS.iterateUntil(NEXT_FUNCTION, LAST_RECORD_INDICATOR)
                    val actual:  List<Int>     = subject.toList()
                    val expected: Array<Int>   = arrayOf(1, 2, 3, 4)

                    actual.should.contain.all.elements(*expected)
                    actual.should.have.a.size.be.equal(expected.size)
                }
            }
        }
    }

    describe("\"iterateWhile\" extension behavior") {

        given("any object that produces a collection of <T> values") {

            it("should be treated as an Iterable<T> while condition is true") {

                val subject: Iterable<Int> = SERIES_OF_NUMBERS.iterateWhile(NEXT_FUNCTION) { it != LAST_RECORD_INDICATOR }
                val actual:  List<Int>     = subject.toList()
                val expected: Array<Int>   = arrayOf(1, 2, 3, 4)

                actual.should.contain.all.elements(*expected)
                actual.should.have.a.size.be.equal(expected.size)
            }
        }
    }

    describe("\"InputStream\" \"toIterable\" behavior") {

    }
}) {

    companion object {

        private const val LAST_RECORD_INDICATOR: Int = -1

        private val NEXT_FUNCTION: KFunction1<SomeSeriesOfValues<Int>, Int> = SomeSeriesOfValues<Int>::nextValue

        private val SERIES_OF_NUMBERS: SomeSeriesOfValues<Int> get() = SomeSeriesOfValues(5, LAST_RECORD_INDICATOR, 4, 3, 2, 1)
    }

    private class SomeSeriesOfValues<out V>(vararg values: V) {

        private val _queue : Stack<V> = Stack()

        init {
            values.forEach { _queue.push(it) }
        }

        fun nextValue(): V = _queue.pop()
    }
}
