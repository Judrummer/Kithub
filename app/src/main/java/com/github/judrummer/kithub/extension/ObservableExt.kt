package com.github.judrummer.kithub.extension

import com.github.kittinunf.result.Result
import rx.Observable
import rx.Observer
import rx.Subscription
import rx.subjects.Subject

fun <T> Observable<T?>.filterNotNull() = filter { it != null }.map { it!! }

fun <T : Any, E : Exception> Observable<Result<T, E>>.filterResultSuccess() = filter { it is Result.Success }.map { (it as Result.Success).value }
fun <T : Any, E : Exception> Observable<Result<T, E>>.filterResultFailure() = filter { it is Result.Failure }.map { (it as Result.Failure).error }

fun <V : Any, E : Exception, V2 : Any, E2 : Exception, O2 : Observable<Result<V2, E2>>> Observable<Result<V, E>>.switchMapResult(next: (V) -> O2) = switchMap {
    it.fold({ next.invoke(it) }, { Observable.just(Result.error(it)) })
}

fun <T : Any> Observable<T>.mapResult() = map { Result.of(it) }.onErrorReturn { Result.error(it as Exception) }

fun <T : Any> Observable<T>.bindSubject(subject: Subject<T, T>) = subscribe { subject.onNext(it) }

fun <T : R, R, X> Observable<T>.rx_bindNext(next: (R) -> X): Subscription {
    return subscribe(object : Observer<T> {
        override fun onNext(t: T) {
            next(t)
        }

        override fun onError(e: Throwable?) {
        }

        override fun onCompleted() {
        }
    })
}

fun <T, X> Observable<T>.rx_bindNext(next: () -> X): Subscription {
    return subscribe(object : Observer<T> {
        override fun onNext(t: T) {
            next()
        }

        override fun onError(e: Throwable?) {
        }

        override fun onCompleted() {
        }
    })
}

fun <T : Any> Observable<T>.share(block: Observable<T>.() -> Unit) = share().apply {
    block()
}!!