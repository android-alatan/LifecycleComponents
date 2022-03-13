package io.androidalatan.component.view.compose.api.textfield

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.androidalatan.component.view.compose.api.ComposeViewInteractionTrigger
import io.androidalatan.component.view.compose.api.ViewEvent
import io.androidalatan.component.view.compose.api.holder.Holdable

data class OnTextChanged(val newText: String) : ViewEvent, Holdable

data class TextTriggerEvent(val text: String, val textChangeEvent: (String) -> Unit)

@Composable
fun ComposeViewInteractionTrigger?.textTriggerEvent(id: Int, initValue: String): TextTriggerEvent {

    var textValue by remember { mutableStateOf(initValue) }

    LaunchedEffect(this) {
        this@textTriggerEvent?.triggerEvent(id, OnTextChanged(textValue))
    }

    return TextTriggerEvent(textValue) { newText ->
        textValue = newText
        this?.triggerEvent(id, OnTextChanged(newText))
    }
}