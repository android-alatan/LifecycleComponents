package io.androidalatan.view.event.legacy.flow.textview

import io.androidalatan.view.event.api.textview.OnTextChangeEvent
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun OnTextChangeEvent.onTextChangeAsFlow(): Flow<OnTextChangeEvent.TextChangeInfo> {
    return callbackFlow {
        val callback = OnTextChangeEvent.Callback { textChangeInfo ->
            trySend(textChangeInfo)
        }
        registerOnTextChangeEvent(callback)

        awaitClose {
            unregisterOnTextChangeEvent(callback)
        }
    }
}