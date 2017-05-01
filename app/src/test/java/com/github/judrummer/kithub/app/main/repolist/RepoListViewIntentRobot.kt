package com.github.judrummer.kithub.app.main.repolist

import com.github.judrummer.kithub.data.entity.RepoEntity
import com.github.kittinunf.result.Result
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import io.reactivex.subscribers.TestSubscriber


/**
 * Created by judrummer on 19/3/2560.
 */

class RepoListViewIntentRobot : RepoListContract.ViewIntent {

    override val refreshIntent = PublishSubject.create<Unit>()
    val subscriptions = CompositeDisposable()

    val getReposSubject = PublishSubject.create<List<RepoEntity>>()!!

    val viewModel by lazy { RepoListViewModel(this, getRepos = { getReposSubject }) }

    fun run(testBlock: RepoListViewIntentRobot.() -> (Unit)) {
        viewModel.attachView()
        this.testBlock()
        viewModel.detachView()
        subscriptions.clear()
    }

}
