package io.androidalatan.lifecycle.compose.handler.sample

import android.app.Application
import android.util.Log
import io.androidalatan.lifecycle.handler.invokeradapter.api.InvokeAdapterInitializer
import io.androidalatan.lifecycle.handler.invokeradapter.flow.FlowInvokeAdapter
import io.androidalatan.lifecycle.handler.invokeradapter.rxjava.RxInvokeAdapter
import io.reactivex.rxjava3.schedulers.Schedulers

class SampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        InvokeAdapterInitializer.initialize(
            factories = listOf(FlowInvokeAdapter.Factory(), RxInvokeAdapter.Factory(Schedulers.computation())),
            errorLogger = { Log.e("InvokeManager", "InvokeManager got error", it) }
        )
    }
}