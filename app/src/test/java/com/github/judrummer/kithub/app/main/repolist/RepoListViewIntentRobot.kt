package com.github.judrummer.kithub.app.main.repolist

import com.github.judrummer.kithub.data.entity.RepoEntity
import com.github.kittinunf.reactiveandroid.rx.addTo
import com.github.kittinunf.result.Result
import rx.observers.TestSubscriber
import rx.subjects.PublishSubject
import rx.subscriptions.CompositeSubscription

/**
 * Created by judrummer on 19/3/2560.
 */

class RepoListViewIntentRobot : RepoListContract.ViewIntent {

    override val refreshIntent = PublishSubject.create<Unit>()
    override val searchIntent = PublishSubject.create<String>()
    override val subscriptions = CompositeSubscription()

    val getReposSubject = PublishSubject.create<Result<List<RepoEntity>, Exception>>()!!
    val searchReposSubject = PublishSubject.create<Result<List<RepoEntity>, Exception>>()!!

    val viewModel = RepoListViewModel(this,
            getRepos = { getReposSubject },
            searchRepos = { searchReposSubject })

    val reposSubscriber = TestSubscriber<List<RepoListContract.RepoItem>>()
    val loadingSubscriber = TestSubscriber<Boolean>()
    val errorSubscriber = TestSubscriber<Exception>()


    fun run(testBlock: RepoListViewIntentRobot.() -> (Unit)) {
        viewModel.attachView()
        viewModel.repos.subscribe(reposSubscriber).addTo(subscriptions)
        viewModel.loading.subscribe(loadingSubscriber).addTo(subscriptions)
        viewModel.error.subscribe(errorSubscriber).addTo(subscriptions)
        this.testBlock()
        viewModel.detachView()
        subscriptions.clear()
    }
}
