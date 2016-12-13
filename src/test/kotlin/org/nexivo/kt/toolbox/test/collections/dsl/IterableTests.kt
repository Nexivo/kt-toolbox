package org.nexivo.kt.toolbox.test.collections.dsl

import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.nexivo.kt.specifics.collections.forEach

class IterableTests: Spek({

    describe("\"forEach\" extension behavior") {

        given("an Iterable<T>") {

            on ("\"forEach\"") {

                it("should run through the Iterable<T> iteration") {

                    val iterable: Iterable<Int> = listOf(1, 2, 3, 4)
                    var actual:   Int           = 0
                    val expected: Int           = 10

                    iterable forEach { actual += it }

                    actual.should.be.equal(expected)
                }
            }
        }

        given("an Iterator<T>") {

            on ("\"forEach\"") {

                it("should run through Iterator<T> iteration") {

                    val iterator: Iterator<Int> = listOf(1, 2, 3, 4).iterator()
                    var actual:   Int           = 0
                    val expected: Int           = 10

                    iterator forEach { actual += it }

                    actual.should.be.equal(expected)
                }
            }
        }
    }
})
