package io.androidalatan.view.event.legacy.textview

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import io.androidalatan.view.event.api.textview.OnTextChangeEvent

class OnTextChangeEventImpl(private val textView: TextView) : OnTextChangeEvent {

    private val callbacks = mutableListOf<OnTextChangeEvent.Callback>()

    private var textWatcher: TextWatcher? = null
    override fun registerOnTextChangeEvent(callback: OnTextChangeEvent.Callback) {
        if (callbacks.isEmpty()) {
            val textWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // do nothing
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    (callbacks.size - 1 downTo 0).map { callbacks[it] }.forEach {
                        it.onTextChange(
                            OnTextChangeEvent.TextChangeInfo(
                                text = s?.toString() ?: "",
                                start = start,
                                count = count,
                                beforeOrAfter = before
                            )
                        )
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    // do nothing
                }
            }

            textView.addTextChangedListener(textWatcher)
            this.textWatcher = textWatcher
        }

        callbacks.add(callback)
        val text = textView.text.toString()
        callback.onTextChange(
            OnTextChangeEvent.TextChangeInfo(
                text = text,
                count = text.length,
            )
        )
    }

    override fun unregisterOnTextChangeEvent(callback: OnTextChangeEvent.Callback) {
        callbacks.remove(callback)
        if (callbacks.isEmpty()) {
            textView.removeTextChangedListener(textWatcher)
            this.textWatcher = null
        }
    }
}