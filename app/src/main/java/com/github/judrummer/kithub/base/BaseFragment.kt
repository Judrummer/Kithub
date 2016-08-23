package com.github.judrummer.kithub.base

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.taskworld.kxandroid.unSafeLazy
import rx.Observable
import rx.subjects.BehaviorSubject

abstract class BaseFragment : Fragment() {

    enum class LifecycleEvent {
        ATTACH,
        CREATE,
        CREATE_VIEW,
        START,
        RESUME,
        PAUSE,
        STOP,
        DESTROY_VIEW,
        DESTROY,
        DETACH
    }
    abstract val contentLayoutResourceId: Int
    private val lifecycleSubject = BehaviorSubject.create<LifecycleEvent>()
    val rootView by lazy(LazyThreadSafetyMode.NONE) { view!! }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        lifecycleSubject.onNext(LifecycleEvent.ATTACH)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleSubject.onNext(LifecycleEvent.CREATE)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(contentLayoutResourceId, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleSubject.onNext(LifecycleEvent.CREATE_VIEW)
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

    override fun onDestroyView() {
        lifecycleSubject.onNext(LifecycleEvent.DESTROY_VIEW)
        super.onDestroyView()
    }

    override fun onDestroy() {
        lifecycleSubject.onNext(LifecycleEvent.DESTROY)
        super.onDestroy()
    }

    override fun onDetach() {
        lifecycleSubject.onNext(LifecycleEvent.DETACH)
        super.onDetach()
    }

    fun lifecycle() = lifecycleSubject.asObservable()

    fun lifecycle(event: LifecycleEvent) = lifecycleSubject.filter { it == event }

    fun <T> Observable<T>.untilEvent(event: LifecycleEvent) = takeUntil(lifecycle(event))

    fun <T> Observable<T>.untilPause() = untilEvent(LifecycleEvent.PAUSE)

    fun <T> Observable<T>.untilStop() = untilEvent(LifecycleEvent.STOP)

    fun <T> Observable<T>.untilDestroy() = untilEvent(LifecycleEvent.DESTROY)

    fun <T> Observable<T>.untilDestroyView() = untilEvent(LifecycleEvent.DESTROY_VIEW)

    fun <T> Observable<T>.untilDetach() = untilEvent(LifecycleEvent.DETACH)

}

