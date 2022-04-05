package io.androidalatan.lifecycle.handler.invokeradapter.rxjava

import androidx.lifecycle.Lifecycle
import io.androidalatan.lifecycle.handler.invokeradapter.api.ErrorLogger
import io.androidalatan.lifecycle.handler.invokeradapter.api.InvokeAdapter
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.internal.functions.Functions
import java.lang.reflect.Type

class RxInvokeAdapter(
    private val scheduler: Scheduler,
    private val logger: ErrorLogger
) : InvokeAdapter<Disposable> {

    override fun acceptableInvoke(returnType: Type): Boolean {
        return returnType in INVOKE_TYPE
    }

    override fun convertInvoke(body: () -> Any): Disposable {
        return when (val bodyValue = body()) {
            is Observable<*> -> {
                Observable.defer { bodyValue }
                    .subscribeOn(scheduler)
                    .subscribe(Functions.emptyConsumer(), logger::e)
            }
            is Single<*> -> {
                Single.defer { bodyValue }
                    .subscribeOn(scheduler)
                    .subscribe(Functions.emptyConsumer(), logger::e)
            }
            is Completable -> {
                Completable.defer { bodyValue }
                    .subscribeOn(scheduler)
                    .subscribe(Functions.EMPTY_ACTION, logger::e)
            }
            is Flowable<*> -> {
                Flowable.defer { bodyValue }
                    .subscribeOn(scheduler)
                    .subscribe(Functions.emptyConsumer(), logger::e)
            }
            is Maybe<*> -> {
                Maybe.defer { bodyValue }
                    .subscribeOn(scheduler)
                    .subscribe(Functions.emptyConsumer(), logger::e)
            }
            else -> Disposable.disposed()
        }
    }

    override fun acceptableRevoke(invoked: Any): Boolean {
        return invoked is Disposable
    }

    override fun convertRevoke(invoked: Any) {
        (invoked as? Disposable)?.dispose()
    }

    companion object {
        private val INVOKE_TYPE =
            hashSetOf<Type>(Observable::class.java, Single::class.java, Completable::class.java, Flowable::class.java, Maybe::class.java)
    }

    class Factory(private val scheduler: Scheduler) : InvokeAdapter.Factory<Disposable> {
        override fun create(lifecycle: Lifecycle, logger: ErrorLogger): InvokeAdapter<Disposable> {
            return RxInvokeAdapter(scheduler, logger)
        }
    }

}