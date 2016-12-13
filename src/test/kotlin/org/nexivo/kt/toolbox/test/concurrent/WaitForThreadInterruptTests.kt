package org.nexivo.kt.toolbox.test.concurrent

import com.winterbe.expekt.should
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.nexivo.kt.specifics.flow.otherwise
import org.nexivo.kt.toolbox.concurrent.waitForThreadInterrupt
import kotlin.concurrent.thread

class WaitForThreadInterruptTests : Spek({

    describe("\"waitForThreadInterrupt\" behavior") {

        given ("a thread context") {

            given("a thread context with a call to \"waitForThreadInterrupt\"") {

                given("it is interrupted before checking the result") {

                    it("it should have the expected ending signal, in this case a specific string") {

                        var actual:   String = "this needs to change"
                        val expected: String = "Done Waiting!"
                        val subject:  Thread = thread {

                            waitForThreadInterrupt() otherwise {
                                throw AssertionError("Thread should not have interrupted itself!")
                            }

                            actual = "Done Waiting!"
                        }

                        thread {

                            Thread.sleep(20)

                            subject.interrupt()
                        }

                        Thread.sleep(200)

                        actual.should.be.equal(expected)
                    }
                }

                given("it is interrupted after checking the result") {

                    it("it should NOT have the expected ending signal, in this case a specific string") {

                        var actual:   String = "this needs to change"
                        val expected: String = "Done Waiting!"
                        val subject:  Thread = thread {

                            waitForThreadInterrupt() otherwise {
                                throw AssertionError("Thread should not have interrupted itself!")
                            }

                            actual = "Done Waiting!"
                        }

                        thread {

                            Thread.sleep(20)

                            subject.interrupt()
                        }

                        actual.should.not.be.equal(expected)
                    }
                }
            }

            given("a thread context with a call to \"waitForThreadInterrupt\" with an self interruption logic") {

                given("it self interrupted before it was externally interrupted") {

                    it("it should have the expected ending signal, in this case a specific string") {

                        var actual:   String = "this needs to change"
                        val expected: String = "Done Waiting!"
                        val subject:  Thread = thread {

                            waitForThreadInterrupt {
                                true
                            } otherwise {
                                actual = "Done Waiting!"
                            }

                            if (actual != "Done Waiting!") {
                                throw AssertionError("Thread should not have been interrupted externally!")
                            }
                        }

                        thread {

                            Thread.sleep(20)

                            subject.interrupt()
                        }

                        Thread.sleep(200)

                        actual.should.be.equal(expected)
                    }
                }

                given("it is self interrupted after checking the result") {

                    it("it should NOT have the expected ending signal, in this case a specific string") {

                        var actual:   String = "this needs to change"
                        val expected: String = "Done Waiting!"

                        thread {

                            waitForThreadInterrupt {
                                true
                            } otherwise {
                                actual = "Done Waiting!"
                            }

                            if (actual != "Done Waiting!") {
                                throw AssertionError("Thread should not have been interrupted externally!")
                            }
                        }

                        actual.should.not.be.equal(expected)
                    }
                }
            }
        }
    }
})
