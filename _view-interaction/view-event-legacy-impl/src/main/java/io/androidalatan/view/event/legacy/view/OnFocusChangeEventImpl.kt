package io.androidalatan.view.event.legacy.view

import android.view.View
import io.androidalatan.view.event.api.view.OnFocusChangeEvent

class OnFocusChangeEventImpl(private val view: View) : OnFocusChangeEvent {

    private val callbacks = mutableListOf<OnFocusChangeEvent.Callback>()

    override fun registerOnFocusChangeEvent(callback: OnFocusChangeEvent.Callback) {
        if (callbacks.isEmpty()) {
            val onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                (callbacks.size - 1 downTo 0).map { callbacks[it] }
                    .forEach { it.onFocusChange(hasFocus) }
            }
            view.onFocusChangeListener = onFocusChangeListener
        }

        callbacks.add(callback)
        callback.onFocusChange(view.isFocused)
    }

    override fun unregisterOnFocusChangeEvent(callback: OnFocusChangeEvent.Callback) {
        callbacks.remove(callback)
        if (callbacks.isEmpty()) {
            view.onFocusChangeListener = null
        }
    }
}