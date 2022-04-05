package io.androidalatan.view.event.legacy.view

import android.view.View
import io.androidalatan.view.event.api.view.OnLongClickEvent

class OnLongClickEventImpl(private val view: View) : OnLongClickEvent {
    private val callbacks = mutableListOf<OnLongClickEvent.Callback>()
    override fun registerOnLongClickEvent(callback: OnLongClickEvent.Callback) {
        if (callbacks.isEmpty()) {
            view.setOnClickListener {
                (callbacks.size - 1 downTo 0).map { callbacks[it] }
                    .forEach(OnLongClickEvent.Callback::onLongClick)
            }
        }
        callbacks.add(callback)

    }

    override fun unregisterOnLongClickEvent(callback: OnLongClickEvent.Callback) {
        callbacks.remove(callback)
        if (callbacks.isEmpty()) {
            view.setOnClickListener(null)
        }
    }
}