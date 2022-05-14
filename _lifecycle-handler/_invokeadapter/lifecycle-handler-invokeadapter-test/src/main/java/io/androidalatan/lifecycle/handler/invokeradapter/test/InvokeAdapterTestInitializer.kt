package io.androidalatan.lifecycle.handler.invokeradapter.test

import io.androidalatan.lifecycle.handler.invokeradapter.api.InvokeAdapterInitializer
import io.androidalatan.lifecycle.handler.invokeradapter.flow.FlowInvokeAdapter
import io.androidalatan.lifecycle.handler.invokeradapter.rxjava.RxInvokeAdapter
import io.reactivex.rxjava3.core.Scheduler

fun invokeAdapterInitializer(scheduler: Scheduler) {
    InvokeAdapterInitializer.initialize(
        factories = listOf(FlowInvokeAdapter.Factory(), RxInvokeAdapter.Factory(scheduler)),
        errorLogger = { }
    )
}