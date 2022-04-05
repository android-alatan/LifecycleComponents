package io.androidalatan.lifecycle.handler.invokeradapter.rxjava

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class RxInvokeAdapterTest {

    private val adapter = RxInvokeAdapter(Schedulers.trampoline()) {}

    @Test
    fun acceptableInvoke() {
        Assertions.assertTrue(adapter.acceptableInvoke(Observable::class.java))
        Assertions.assertTrue(adapter.acceptableInvoke(Flowable::class.java))
        Assertions.assertTrue(adapter.acceptableInvoke(Single::class.java))
        Assertions.assertTrue(adapter.acceptableInvoke(Maybe::class.java))
        Assertions.assertTrue(adapter.acceptableInvoke(Completable::class.java))

        Assertions.assertFalse(adapter.acceptableInvoke(Flow::class.java))

        Assertions.assertFalse(adapter.acceptableInvoke(Unit::class.java))
        Assertions.assertFalse(adapter.acceptableInvoke(Int::class.java))
        Assertions.assertFalse(adapter.acceptableInvoke(Boolean::class.java))
        Assertions.assertFalse(adapter.acceptableInvoke(String::class.java))
    }

    @Test
    fun `convertInvoke proper type and body`() {
        var count = 0
        adapter.convertInvoke {
            Observable.never<Boolean>()
                .doOnSubscribe { count++ }
        }
            .dispose()

        Assertions.assertEquals(1, count)
    }

    @Test
    fun `convertInvoke wrong type and body`() {
        var count = 0
        adapter.convertInvoke {
            Observable.never<Boolean>()
                .doOnSubscribe { count++ }
        }
            .dispose()

        Assertions.assertEquals(1, count)
    }

    @Test
    fun acceptableRevoke() {
        Assertions.assertTrue(adapter.acceptableRevoke(Disposable.disposed()))

        Assertions.assertFalse(adapter.acceptableRevoke(Observable.just(true)))
        Assertions.assertFalse(adapter.acceptableRevoke(Flowable.just(true)))
        Assertions.assertFalse(adapter.acceptableRevoke(Single.just(true)))
        Assertions.assertFalse(adapter.acceptableRevoke(Maybe.just(true)))
        Assertions.assertFalse(adapter.acceptableRevoke(Completable.complete()))

        Assertions.assertFalse(adapter.acceptableRevoke(Unit::class.java))
        Assertions.assertFalse(adapter.acceptableRevoke(Int::class.java))
        Assertions.assertFalse(adapter.acceptableRevoke(Boolean::class.java))
        Assertions.assertFalse(adapter.acceptableRevoke(String::class.java))
    }

    @Test
    fun `convertRevoke disposable`() {
        var count = 0
        adapter.convertRevoke(Observable.never<Boolean>()
                                  .doOnDispose {
                                      count++
                                  }
                                  .subscribe())
        Assertions.assertEquals(1, count)
    }

    @Test
    fun `convertRevoke non disposable`() {
        var count = 0
        adapter.convertRevoke(Observable.never<Boolean>()
                                  .doOnDispose {
                                      count++
                                  })
        Assertions.assertEquals(0, count)

    }
}