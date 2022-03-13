package io.androidalatan.component.view.compose.api.compoundbutton

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import io.androidalatan.component.view.compose.api.ComposeViewInteractionTrigger
import io.androidalatan.component.view.compose.api.ViewEvent
import io.androidalatan.component.view.compose.api.holder.Holdable

data class OnCheckedChanged(val checked: Boolean) : ViewEvent, Holdable

@Composable
fun ComposeViewInteractionTrigger?.onCheckedChanged(id: Int, initValue: Boolean): (Boolean) -> Unit {

    LaunchedEffect(this) {
        this@onCheckedChanged?.triggerEvent(id, OnCheckedChanged(initValue))
    }

    return { checked ->
        this?.triggerEvent(id, OnCheckedChanged(checked))
    }
}