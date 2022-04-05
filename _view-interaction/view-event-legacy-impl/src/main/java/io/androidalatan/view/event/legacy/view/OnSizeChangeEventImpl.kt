package io.androidalatan.view.event.legacy.view

import android.view.View
import android.view.ViewTreeObserver
import io.androidalatan.view.event.api.view.OnSizeChangeEvent

class OnSizeChangeEventImpl(private val view: View) : OnSizeChangeEvent {
    private val callbacks = mutableListOf<OnSizeChangeEvent.Callback>()
    private var listener: ViewTreeObserver.OnGlobalLayoutListener? = null

    override fun registerOnSizeChangeCallback(callback: OnSizeChangeEvent.Callback) {
        initListenerIfNeed()
        callbacks.add(callback)
        callback.onSizeChange(OnSizeChangeEvent.ViewSize(view.width, view.height))
    }

    override fun unregisterOnSizeChangeCallback(callback: OnSizeChangeEvent.Callback) {
        callbacks.remove(callback)
        deinitListenerIfNeed()
    }

    private fun initListenerIfNeed() {
        if (callbacks.isEmpty()) {
            val listener = ViewTreeObserver.OnGlobalLayoutListener {
                (callbacks.size - 1 downTo 0).map { callbacks[it] }.forEach {
                    it.onSizeChange(
                        OnSizeChangeEvent.ViewSize(
                            view.width,
                            view.height
                        )
                    )
                }
            }
            view.viewTreeObserver.addOnGlobalLayoutListener(listener)
            this.listener = listener
        }
    }

    private fun deinitListenerIfNeed() {
        if (callbacks.isEmpty()) {
            if (view.viewTreeObserver.isAlive && listener != null) {
                view.viewTreeObserver.removeOnGlobalLayoutListener(listener)
            }
            listener = null
        }
    }
}