package io.androidalatan.view.event.legacy.tablayout

import com.google.android.material.tabs.TabLayout
import io.androidalatan.view.event.api.tablayout.OnTabSelectEvent

class OnTabSelectEventImpl(private val tabLayout: TabLayout) : OnTabSelectEvent {
    private val callbacks = mutableListOf<OnTabSelectEvent.Callback>()

    private var listener: TabLayout.OnTabSelectedListener? = null

    override fun registerOnTabSelectCallback(callback: OnTabSelectEvent.Callback) {
        if (callbacks.isEmpty()) {
            val listener = object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    (callbacks.size - 1 downTo 0).map { callbacks[it] }.forEach {
                        it.onTabSelect(
                            OnTabSelectEvent.SelectedTab(
                                tab.position,
                                OnTabSelectEvent.SelectedEventType.SELECTED
                            )
                        )
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {
                    (callbacks.size - 1 downTo 0).map { callbacks[it] }.forEach {
                        it.onTabSelect(
                            OnTabSelectEvent.SelectedTab(
                                tab.position,
                                OnTabSelectEvent.SelectedEventType.UNSELECTED
                            )
                        )
                    }
                }

                override fun onTabReselected(tab: TabLayout.Tab) {
                    (callbacks.size - 1 downTo 0).map { callbacks[it] }.forEach {
                        it.onTabSelect(
                            OnTabSelectEvent.SelectedTab(
                                tab.position,
                                OnTabSelectEvent.SelectedEventType.RESELECTED
                            )
                        )
                    }
                }
            }
            tabLayout.addOnTabSelectedListener(listener)
            this.listener = listener
        }

        callbacks.add(callback)
    }

    override fun unregisterOnTabSelectCallback(callback: OnTabSelectEvent.Callback) {
        callbacks.remove(callback)
        if (callbacks.isEmpty()) {
            tabLayout.removeOnTabSelectedListener(listener as TabLayout.OnTabSelectedListener)
        }
    }
}