package io.androidalatan.component.view.compose.api.textfield

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import io.androidalatan.component.view.compose.api.ComposeViewInteractionTrigger
import io.androidalatan.component.view.compose.api.ViewEvent
import io.androidalatan.component.view.compose.api.holder.Holdable

data class OnFocusChanged(val focused: Boolean) : ViewEvent, Holdable

@Composable
fun <T> ComposeViewInteractionTrigger?.onFocusChanged(
    id: Int,
    initValue: Boolean,
    focusedCalculation: (T) -> Boolean
): (T) -> Unit {
    LaunchedEffect(this) {
        this@onFocusChanged?.triggerEvent(id, OnFocusChanged(initValue))
    }

    return { focusedValue ->
        this?.triggerEvent(id, OnFocusChanged(focusedCalculation.invoke(focusedValue)))
    }
}