package com.github.judrummer.kithub.base

import android.os.Bundle
import io.reactivex.Observable

interface BaseViewModel<T> {
    val state: Observable<T>
    fun attachView()
    fun detachView()
    fun saveState(): T
    fun restoreState(state: T)
}