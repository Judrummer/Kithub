package com.github.judrummer.kithub.app.main.repolist

import com.github.judrummer.jxadapter.JxItem
import com.github.judrummer.kithub.data.entity.RepoEntity
import com.github.judrummer.kithub.data.usecase.*
import com.github.judrummer.kithub.extension.*
import com.taskworld.kxandroid.logD
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import kotlin.properties.Delegates


class RepoListViewModel(
        private val viewIntent: RepoListContract.ViewIntent,
        private val getRepos: GetRepos = GetReposImpl
) : RepoListContract.ViewModel {

    private var stateProps: RepoListContract.State by Delegates.observable(RepoListContract.State()) { _, _, new ->
        state.onNext(new)
    }

    override val state = BehaviorSubject.createDefault<RepoListContract.State>(stateProps)
    override val showError = BehaviorSubject.create<Exception>()!!
    private val subscriptions = CompositeDisposable()

    override fun attachView() {
        println("VM attachView")
        viewIntent.refreshIntent
                .switchMap {
                    getRepos()
                            .map { stateProps.copy(loading = false, repos = it.map(this::mapRepoToRepoItem)) }
                            .onErrorReturn {
                                println("On Error ${it.message ?: ""}")
                                showError.onNext(it as Exception)
                                stateProps.copy(loading = false, error = it)
                            }
                            .startWith(stateProps.copy(loading = true, error = null))
                }
                .subscribe {
                    stateProps = it
                }.addTo(subscriptions)

    }

    private fun mapRepoToRepoItem(repo: RepoEntity) = RepoListContract.RepoItem(repo.id,
            repo.name,
            repo.description ?: "",
            repo.stargazers_count)

    override fun detachView() {
        subscriptions.clear()
    }

}

