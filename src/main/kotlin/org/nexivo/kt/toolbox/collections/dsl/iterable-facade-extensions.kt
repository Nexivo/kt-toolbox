package org.nexivo.kt.toolbox.collections.dsl

import org.nexivo.kt.toolbox.collections.IterableFacade

fun <T, V> T.iterateUntil(next: T.() -> V, predicate: (V) -> Boolean): Iterable<V> = IterableFacade(this, next, { !predicate(it) })

fun <T, V> T.iterateUntil(next: T.() -> V, signal: V): Iterable<V> = IterableFacade(this, next, { it != signal })

fun <T, V> T.iterateWhile(next: T.() -> V, predicate: (V) -> Boolean = { true }): Iterable<V> = IterableFacade(this, next, predicate)

