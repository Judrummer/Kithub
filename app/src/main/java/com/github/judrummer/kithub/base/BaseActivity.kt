package com.github.judrummer.kithub.base

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import rx.Observable
import rx.subjects.BehaviorSubject

abstract class BaseActivity : AppCompatActivity() {

    enum class LifecycleEvent {
        CREATE,
        START,
        RESUME,
        PAUSE,
        STOP,
        DESTROY
    }
    abstract val contentLayoutResourceId: Int
    private val lifecycleSubject = BehaviorSubject.create<LifecycleEvent>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleSubject.onNext(LifecycleEvent.CREATE)
        if (contentLayoutResourceId != 0) {
            setContentView(contentLayoutResourceId)
        }
    }

    override fun onStart() {
        super.onStart()
        lifecycleSubject.onNext(LifecycleEvent.START)
    }

    override fun onResume() {
        super.onResume()
        lifecycleSubject.onNext(LifecycleEvent.RESUME)
    }

    override fun onPause() {
        lifecycleSubject.onNext(LifecycleEvent.PAUSE)
        super.onPause()
    }

    override fun onStop() {
        lifecycleSubject.onNext(LifecycleEvent.STOP)
        super.onStop()
    }

    override fun onDestroy() {
        lifecycleSubject.onNext(LifecycleEvent.DESTROY)
        super.onDestroy()
    }


    fun lifecycle() = lifecycleSubject.asObservable()

    fun lifecycle(event: LifecycleEvent) = lifecycleSubject.filter { it == event }

    fun <T> Observable<T>.untilEvent(event: LifecycleEvent) = takeUntil(lifecycle(event))

    fun <T> Observable<T>.untilPause() = untilEvent(LifecycleEvent.PAUSE)

    fun <T> Observable<T>.untilStop() = untilEvent(LifecycleEvent.STOP)

    fun <T> Observable<T>.untilDestroy() = untilEvent(LifecycleEvent.DESTROY)

}