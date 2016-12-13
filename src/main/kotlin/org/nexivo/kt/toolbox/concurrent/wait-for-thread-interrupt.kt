package org.nexivo.kt.toolbox.concurrent

private const val DEFAULT_SLEEP_INTERVAL: Long = 10

fun waitForThreadInterrupt(sleepInterval: Long = DEFAULT_SLEEP_INTERVAL, interrupt: (() -> Boolean) = { false }): Boolean {

    while (true) {
        try {
            if (interrupt.invoke()) { return@waitForThreadInterrupt false }

            Thread.sleep(sleepInterval)
        } catch (ex: InterruptedException) {
            return@waitForThreadInterrupt true
        }
    }
}