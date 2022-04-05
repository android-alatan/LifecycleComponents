package io.androidalatan.view.event.legacy.compoundbutton

import android.widget.CompoundButton
import io.androidalatan.view.event.api.compoundbutton.OnCheckedChangeEvent

class OnCheckedChangeEventImpl(private val view: CompoundButton) : OnCheckedChangeEvent {
    private val callbacks = mutableListOf<OnCheckedChangeEvent.Callback>()

    override fun registerOnCheckChangeCallback(callback: OnCheckedChangeEvent.Callback) {
        if (callbacks.isEmpty()) {
            view.setOnCheckedChangeListener { _, isChecked ->
                (callbacks.size - 1 downTo 0).map { callbacks[it] }
                    .forEach { it.onCheckChange(isChecked) }
            }
        }

        callbacks.add(callback)
        callback.onCheckChange(view.isChecked)
    }

    override fun unregisterOnCheckChangeCallback(callback: OnCheckedChangeEvent.Callback) {
        callbacks.remove(callback)
        if (callbacks.isEmpty()) {
            view.setOnCheckedChangeListener(null)
        }
    }
}