package com.github.judrummer.kithub.app.main.repolist

import com.github.judrummer.kithub.data.entity.RepoEntity
import com.github.judrummer.kithub.data.usecase.*
import com.github.judrummer.kithub.extension.*
import com.taskworld.kxandroid.logD
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import kotlin.properties.Delegates


class RepoListViewModel(
        private val viewIntent: RepoListContract.ViewIntent,
        private val getRepos: GetRepos = GetReposImpl
) : RepoListContract.ViewModel {


    private var stateProps: RepoListContract.State by Delegates.observable(RepoListContract.State()) { _, _, new -> state.onNext(new) }
    private val subscriptions = CompositeDisposable()

    override val state = BehaviorSubject.createDefault<RepoListContract.State>(stateProps)
    override val showError = PublishSubject.create<Exception>()!!

    override fun attachView() {
        viewIntent.refreshIntent
                .switchMap {
                    getRepos()
                            .map { stateProps.copy(loading = false, repos = it.map(this::mapRepoToRepoItem)) }
                            .onErrorReturn {
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

    override fun saveState(): RepoListContract.State = stateProps
    override fun restoreState(state: RepoListContract.State) {
        stateProps = state
    }

}

