package io.androidalatan.view.event.assertions

import io.androidalatan.view.event.api.compoundbutton.CompoundButtonEvent
import io.androidalatan.view.event.api.compoundbutton.OnCheckedChangeEvent
import io.androidalatan.view.event.api.itemview.ItemViewEvent
import io.androidalatan.view.event.api.itemview.OnItemOfListClickEvent
import io.androidalatan.view.event.api.tablayout.OnTabSelectEvent
import io.androidalatan.view.event.api.tablayout.TabLayoutEvent
import io.androidalatan.view.event.api.textview.OnTextChangeEvent
import io.androidalatan.view.event.api.textview.TextViewEvent
import io.androidalatan.view.event.api.toolbar.OnToolbarMenuItemClick
import io.androidalatan.view.event.api.toolbar.OnToolbarNavigationClick
import io.androidalatan.view.event.api.toolbar.ToolbarEvent
import io.androidalatan.view.event.api.view.OnClickEvent
import io.androidalatan.view.event.api.view.OnFocusChangeEvent
import io.androidalatan.view.event.api.view.OnLongClickEvent
import io.androidalatan.view.event.api.view.OnSizeChangeEvent
import io.androidalatan.view.event.api.view.ViewEvent

class MockViewEvent : EventTrigger, ViewEvent, TextViewEvent, CompoundButtonEvent, ItemViewEvent, TabLayoutEvent, ToolbarEvent {
    private val onClicks by lazy { mutableListOf<OnClickEvent.Callback>() }
    private val onLongClicks by lazy { mutableListOf<OnLongClickEvent.Callback>() }
    private val onFocusChanges by lazy { mutableListOf<OnFocusChangeEvent.Callback>() }
    private val onSizeChanges by lazy { mutableListOf<OnSizeChangeEvent.Callback>() }
    private val onTextChanges by lazy { mutableListOf<OnTextChangeEvent.Callback>() }
    private val onCheckeChanges by lazy { mutableListOf<OnCheckedChangeEvent.Callback>() }
    private val onItemClicks by lazy { mutableListOf<OnItemOfListClickEvent.Callback>() }
    private val onTabSelects by lazy { mutableListOf<OnTabSelectEvent.Callback>() }
    private val onMenuItemClicks by lazy { mutableListOf<OnToolbarMenuItemClick.Callback>() }
    private val onNavigationClicks by lazy { mutableListOf<OnToolbarNavigationClick.Callback>() }

    private var focused = false
    private var size = OnSizeChangeEvent.ViewSize(0, 0)
    private var text = OnTextChangeEvent.TextChangeInfo()
    private var checked = false
    private var lastSelectedTab = OnTabSelectEvent.SelectedTab(0, OnTabSelectEvent.SelectedEventType.SELECTED)

    override fun registerOnClickEvent(callback: OnClickEvent.Callback) {
        onClicks.add(callback)
    }

    override fun unregisterOnClickEvent(callback: OnClickEvent.Callback) {
        onClicks.remove(callback)
    }

    override fun click() {
        (onClicks.size - 1 downTo 0).map { onClicks[it] }
            .forEach { it.onClick() }
    }

    override fun registerOnFocusChangeEvent(callback: OnFocusChangeEvent.Callback) {
        onFocusChanges.add(callback)
        callback.onFocusChange(focused)
    }

    override fun unregisterOnFocusChangeEvent(callback: OnFocusChangeEvent.Callback) {
        onFocusChanges.remove(callback)
    }

    override fun newFocus(newFocused: Boolean) {
        if (this.focused != newFocused) {
            this.focused = newFocused
            (onFocusChanges.size - 1 downTo 0).map { onFocusChanges[it] }
                .forEach { it.onFocusChange(newFocused) }
        }
    }

    override fun registerOnLongClickEvent(callback: OnLongClickEvent.Callback) {
        onLongClicks.add(callback)
    }

    override fun unregisterOnLongClickEvent(callback: OnLongClickEvent.Callback) {
        onLongClicks.remove(callback)
    }

    override fun longClick() {
        (onLongClicks.size - 1 downTo 0).map { onLongClicks[it] }
            .forEach { it.onLongClick() }
    }

    override fun registerOnSizeChangeCallback(callback: OnSizeChangeEvent.Callback) {
        onSizeChanges.add(callback)
        callback.onSizeChange(size)
    }

    override fun unregisterOnSizeChangeCallback(callback: OnSizeChangeEvent.Callback) {
        onSizeChanges.remove(callback)
    }

    override fun newSize(newSize: OnSizeChangeEvent.ViewSize) {
        if (this.size != newSize) {
            this.size = newSize
            (onSizeChanges.size - 1 downTo 0).map { onSizeChanges[it] }
                .forEach { it.onSizeChange(newSize) }
        }
    }

    override fun registerOnTextChangeEvent(callback: OnTextChangeEvent.Callback) {
        onTextChanges.add(callback)
        callback.onTextChange(text)
    }

    override fun unregisterOnTextChangeEvent(callback: OnTextChangeEvent.Callback) {
        onTextChanges.remove(callback)
    }

    override fun newText(newText: OnTextChangeEvent.TextChangeInfo) {
        if (this.text != newText) {
            this.text = newText
            (onTextChanges.size - 1 downTo 0).map { onTextChanges[it] }
                .forEach { it.onTextChange(newText) }
        }
    }

    override fun registerOnCheckChangeCallback(callback: OnCheckedChangeEvent.Callback) {
        onCheckeChanges.add(callback)
        callback.onCheckChange(checked)
    }

    override fun unregisterOnCheckChangeCallback(callback: OnCheckedChangeEvent.Callback) {
        onCheckeChanges.remove(callback)
    }

    override fun newCheck(newChecked: Boolean) {
        if (this.checked != newChecked) {
            this.checked = newChecked
            (onCheckeChanges.size - 1 downTo 0).map { onCheckeChanges[it] }
                .forEach { it.onCheckChange(newChecked) }
        }
    }

    override fun registerOnItemClickEvent(callback: OnItemOfListClickEvent.Callback) {
        onItemClicks.add(callback)
    }

    override fun unregisterOnItemClickEvent(callback: OnItemOfListClickEvent.Callback) {
        onItemClicks.remove(callback)
    }

    override fun itemClick(item: OnItemOfListClickEvent.ClickedItem<Any>) {
        (onItemClicks.size - 1 downTo 0).map { onItemClicks[it] }
            .forEach { it.onItemClick(item) }
    }

    override fun registerOnTabSelectCallback(callback: OnTabSelectEvent.Callback) {
        onTabSelects.add(callback)
        callback.onTabSelect(lastSelectedTab)
    }

    override fun unregisterOnTabSelectCallback(callback: OnTabSelectEvent.Callback) {
        onTabSelects.remove(callback)
    }

    override fun newTab(newTab: OnTabSelectEvent.SelectedTab) {
        if (newTab.type == OnTabSelectEvent.SelectedEventType.SELECTED && this.lastSelectedTab.position != newTab.position) {
            (onTabSelects.size - 1 downTo 0).map { onTabSelects[it] }
                .forEach {
                    it.onTabSelect(lastSelectedTab.copy(type = OnTabSelectEvent.SelectedEventType.UNSELECTED))
                    it.onTabSelect(newTab)
                }
            this.lastSelectedTab = newTab
        }
    }

    override fun registerOnMenuItemClickEvent(callback: OnToolbarMenuItemClick.Callback) {
        onMenuItemClicks.add(callback)
    }

    override fun unregisterOnMenuItemClickEvent(callback: OnToolbarMenuItemClick.Callback) {
        onMenuItemClicks.remove(callback)
    }

    override fun menuItemClick(itemId: Int) {
        (onMenuItemClicks.size - 1 downTo 0).map { onMenuItemClicks[it] }
            .forEach { it.onNavigationMenuItemClick(itemId) }
    }

    override fun registerOnNavigationClickEvent(callback: OnToolbarNavigationClick.Callback) {
        onNavigationClicks.add(callback)
    }

    override fun unregisterOnNavigationClickEvent(callback: OnToolbarNavigationClick.Callback) {
        onNavigationClicks.remove(callback)
    }

    override fun onNavClick() {
        (onNavigationClicks.size - 1 downTo 0).map { onNavigationClicks[it] }
            .forEach { it.onToolbarNavigationClick() }
    }
}