package io.androidalatan.lifecycle.handler.sample

import android.app.Application
import android.util.Log
import io.androidalatan.datastore.preference.PreferencesBuilderInitializer
import io.androidalatan.datastore.preference.adapter.api.ClearAdapter
import io.androidalatan.datastore.preference.adapter.api.GetAdapter
import io.androidalatan.datastore.preference.adapter.api.SetAdapter
import io.androidalatan.datastore.preference.adapter.rx.RxClearAdapter
import io.androidalatan.datastore.preference.adapter.rx.RxGetAdapter
import io.androidalatan.datastore.preference.adapter.rx.RxSetAdapter
import io.androidalatan.lifecycle.handler.invokeradapter.api.InvokeAdapterInitializer
import io.androidalatan.lifecycle.handler.invokeradapter.flow.FlowInvokeAdapter
import io.androidalatan.lifecycle.handler.invokeradapter.rxjava.RxInvokeAdapter
import io.androidalatan.rx.scheduler.api.SchedulerProvider

class SampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        InvokeAdapterInitializer.initialize(
            factories = listOf(FlowInvokeAdapter.Factory(), RxInvokeAdapter.Factory(SchedulerProvider().computation())),
            errorLogger = { Log.e("InvokeManager", "InvokeManager got error", it) }
        )

        val schedulerProvider = SchedulerProvider()
        PreferencesBuilderInitializer.init(
            setAdapterFactories = listOf(SetAdapter.Factory {
                RxSetAdapter(schedulerProvider.io())
            }),
            getAdapterFactories = listOf(GetAdapter.Factory {
                RxGetAdapter(it, schedulerProvider.io())
            }),
            clearAdapterFactories = listOf(ClearAdapter.Factory {
                RxClearAdapter(schedulerProvider.io())
            })
        )

    }
}