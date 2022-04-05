package io.androidalatan.view.event.legacy.toolbar

import androidx.appcompat.widget.Toolbar
import io.androidalatan.view.event.api.toolbar.OnToolbarNavigationClick

class OnToolbarNavigationClickImpl(private val toolbar: Toolbar) : OnToolbarNavigationClick {
    private val callbacks = mutableListOf<OnToolbarNavigationClick.Callback>()
    override fun registerOnNavigationClickEvent(callback: OnToolbarNavigationClick.Callback) {
        if (callbacks.isEmpty()) {
            toolbar.setNavigationOnClickListener {
                (callbacks.size - 1 downTo 0).map { callbacks[it] }
                    .forEach { it.onToolbarNavigationClick() }
            }
        }

        callbacks.add(callback)
    }

    override fun unregisterOnNavigationClickEvent(callback: OnToolbarNavigationClick.Callback) {
        callbacks.remove(callback)
        if (callbacks.isEmpty()) {
            toolbar.setOnMenuItemClickListener(null)
        }
    }
}