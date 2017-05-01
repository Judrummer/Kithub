package com.github.judrummer.kithub.app.main.repolist

import com.github.judrummer.jxadapter.JxItem
import com.github.judrummer.kithub.data.entity.RepoEntity
import com.github.judrummer.kithub.data.usecase.*
import com.github.judrummer.kithub.extension.*
import com.taskworld.kxandroid.logD
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import kotlin.properties.Delegates


class RepoListViewModel(
        private val viewIntent: RepoListContract.ViewIntent,
        private val getRepos: GetRepos = GetReposImpl
) : RepoListContract.ViewModel {

    private var stateProps: RepoListContract.State by Delegates.observable(RepoListContract.State()) { _, new, _ ->
        state.onNext(new)
    }

    override val state = PublishSubject.create<RepoListContract.State>()
    override val showError = PublishSubject.create<Exception>()!!
    private val subscriptions = CompositeDisposable()
    override fun attachView() {
        viewIntent.refreshIntent
                .switchMap { getRepos() }
                .map { stateProps.copy(loading = false, repos = it.map(this::mapRepoToRepoItem)) }
                .startWith { stateProps.copy(loading = true) }
                .onErrorReturn {
                    showError.onNext(it as Exception)
                    stateProps.copy(loading = false)
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

