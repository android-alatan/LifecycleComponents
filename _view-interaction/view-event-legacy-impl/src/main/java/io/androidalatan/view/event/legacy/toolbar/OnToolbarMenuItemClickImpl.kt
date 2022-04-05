package io.androidalatan.view.event.legacy.toolbar

import androidx.appcompat.widget.Toolbar
import io.androidalatan.view.event.api.toolbar.OnToolbarMenuItemClick

class OnToolbarMenuItemClickImpl(private val toolbar: Toolbar) : OnToolbarMenuItemClick {
    private val callbacks = mutableListOf<OnToolbarMenuItemClick.Callback>()

    override fun registerOnMenuItemClickEvent(callback: OnToolbarMenuItemClick.Callback) {
        if (callbacks.isEmpty()) {
            toolbar.setOnMenuItemClickListener { item ->
                (callbacks.size - 1 downTo 0).map { callbacks[it] }
                    .forEach { it.onNavigationMenuItemClick(item.itemId) }
                true
            }
        }

        callbacks.add(callback)
    }

    override fun unregisterOnMenuItemClickEvent(callback: OnToolbarMenuItemClick.Callback) {
        callbacks.remove(callback)
        if (callbacks.isEmpty()) {
            toolbar.setOnMenuItemClickListener(null)
        }
    }
}