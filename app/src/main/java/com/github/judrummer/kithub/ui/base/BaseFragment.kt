package com.github.judrummer.kithub.ui.base

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.taskworld.kxandroid.unSafeLazy
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment : Fragment() {


    abstract val contentLayoutResourceId: Int

    val rootView by lazy(LazyThreadSafetyMode.NONE) { view!! }
    val disposables = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(contentLayoutResourceId, container, false)
    }

    override fun onDestroyView() {
        disposables.clear()
        super.onDestroyView()
    }

}

