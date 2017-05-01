package com.github.judrummer.kithub.extension

import com.github.kittinunf.result.Result
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.Subject

fun <T : Any, E : Exception> Observable<Result<T, E>>.filterResultSuccess() = filter { it is Result.Success }.map { (it as Result.Success).value }
fun <T : Any, E : Exception> Observable<Result<T, E>>.filterResultFailure() = filter { it is Result.Failure }.map { (it as Result.Failure).error }

fun <T : Any> Observable<T>.mapResult() = map { Result.of(it) }.onErrorReturn { Result.error(it as Exception) }

fun Disposable.addTo(subscriptions: CompositeDisposable) {
    subscriptions.add(this)
}

fun <T : Any> Observable<T>.share(block: Observable<T>.() -> Unit) = share().apply {
    block()
}!!