package com.github.judrummer.kithub.app.main.repolist

import com.github.judrummer.kithub.data.api.RepoApi
import com.github.judrummer.kithub.data.entity.RepoEntity
import com.github.judrummer.kithub.extension.addTo
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject


/**
 * Created by judrummer on 19/3/2560.
 */

class RepoListViewIntentRobot : RepoListContract.ViewIntent {

    override val refreshIntent = PublishSubject.create<Unit>()
    val disposables = CompositeDisposable()

    val getReposSubject = PublishSubject.create<List<RepoEntity>>()!!
    val searchReposSubject = PublishSubject.create<List<RepoEntity>>()!!

    val viewModel by lazy {
        RepoListViewModel(this, object : RepoApi {
            override fun getRepos(): Observable<List<RepoEntity>> = getReposSubject

            override fun searchRepos(query: String): Observable<List<RepoEntity>> = searchReposSubject

        })
    }

    val stateObserver = TestObserver<RepoListContract.State>()
    val showErrorObserver = TestObserver<Exception>()
    fun run(testBlock: RepoListViewIntentRobot.() -> (Unit)) {
        viewModel.attachView()
        viewModel.state.subscribe(stateObserver)
        stateObserver.addTo(disposables)
        viewModel.showError.subscribe(showErrorObserver)
        showErrorObserver.addTo(disposables)
        this.testBlock()
        viewModel.detachView()
        disposables.clear()
    }

}
