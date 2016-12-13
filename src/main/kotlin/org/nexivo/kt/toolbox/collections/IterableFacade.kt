package org.nexivo.kt.toolbox.collections

class IterableFacade<T, V>(source: T, next: T.() -> V, predicate: ((V) -> Boolean) = { true }): Iterable<V> {

    private val _source:    T              = source
    private val _next:      T.() -> V      = next
    private val _predicate: (V) -> Boolean = predicate

    constructor (source: T, next: T.() -> V, endSignal: V): this(source, next, { it != endSignal })

    inner private class IteratorFacade : Iterator<V> {

        var _nextValue: V? = null

        override fun next(): V
            = if (_nextValue != null) {

                val next = _nextValue!!

                _nextValue = null

                next
            } else {
                throw IndexOutOfBoundsException()
            }

        override fun hasNext(): Boolean {

            if (_nextValue != null) { return true }

            val next:   V       = _source._next()
            val result: Boolean = _predicate(next)

            if (result) { _nextValue = next }

            return result
        }
    }

    override fun iterator(): Iterator<V> = IteratorFacade()
}