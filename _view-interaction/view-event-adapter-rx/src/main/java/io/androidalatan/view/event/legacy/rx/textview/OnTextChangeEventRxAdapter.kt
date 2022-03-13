package io.androidalatan.view.event.legacy.rx.textview

import io.androidalatan.view.event.api.textview.OnTextChangeEvent
import io.reactivex.rxjava3.core.Observable

fun OnTextChangeEvent.onTextChangeAsObservable(): Observable<OnTextChangeEvent.TextChangeInfo> {
    return Observable.create { emitter ->
        val callback = OnTextChangeEvent.Callback { textChangeInfo ->
            emitter.onNext(textChangeInfo)
        }
        registerOnTextChangeEvent(callback)

        emitter.setCancellable {
            unregisterOnTextChangeEvent(callback)
        }
    }
}