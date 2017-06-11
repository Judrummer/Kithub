package com.github.judrummer.kithub.extension

import com.github.kittinunf.result.Result
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction


fun <T, U, R> kx_combineLatest(o1: Observable<T>, o2: Observable<U>, combine: (T, U) -> R) = Observable.combineLatest(o1, o2, BiFunction { t1: T, t2: U -> combine(t1, t2) })
fun <T, U, R> Observable<T>.kx_withLatestFrom(o2: Observable<U>, combine: (T, U) -> R) = this.withLatestFrom(o2, BiFunction { t1: T, t2: U -> combine(t1, t2) })

fun <T : Any, E : Exception> Observable<Result<T, E>>.filterResultSuccess() = filter { it is Result.Success }.map { (it as Result.Success).value }
fun <T : Any, E : Exception> Observable<Result<T, E>>.filterResultFailure() = filter { it is Result.Failure }.map { (it as Result.Failure).error }

fun <T : Any> Observable<T>.mapResult() = map { Result.of(it) }.onErrorReturn { Result.error(it as Exception) }

fun Disposable.addTo(disposables: CompositeDisposable) {
    disposables.add(this)
}

fun <T : Any> Observable<T>.share(block: Observable<T>.() -> Unit) = share().apply {
    block()
}!!