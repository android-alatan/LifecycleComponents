package io.androidalatan.coroutine.test

import app.cash.turbine.FlowTurbine
import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest

@ExperimentalCoroutinesApi
fun <T> Flow<T>.turbine(body: suspend TestScope.(FlowTurbine<T>) -> Unit) = runTest {
    this@turbine.test {
        body(this)
    }
}
