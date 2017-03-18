package com.github.judrummer.kithub.app.main.repolist

import com.github.judrummer.kithub.data.usecase.*
import com.github.judrummer.kithub.extension.bindSubject
import com.github.judrummer.kithub.extension.filterResultFailure
import com.github.judrummer.kithub.extension.filterResultSuccess
import com.github.judrummer.kithub.extension.share
import com.github.kittinunf.reactiveandroid.rx.addTo
import com.taskworld.kxandroid.logD
import rx.Observable
import rx.subjects.BehaviorSubject

class RepoListViewModel(
        private val viewIntent: RepoListContract.ViewIntent,
        private val getRepos: GetRepos = GetReposImpl,
        private val searchRepos: SearchRepos = SearchReposImpl
) : RepoListContract.ViewModel {

    override val repoes = BehaviorSubject.create<List<RepoListContract.RepoItem>>()
    override val loading = BehaviorSubject.create<Boolean>()
    override val error = BehaviorSubject.create<Exception>()

    override fun attachView() {
        Observable.combineLatest(viewIntent.refreshIntent, viewIntent.searchIntent) { _, search -> search }
                .doOnNext {
                    logD("loading true")
                    loading.onNext(true)
                }
                .switchMap { search ->
                    if (search.isNotBlank()) {
                        searchRepos(search)
                    } else {
                        getRepos()
                    }
                }
                .doOnNext {
                    logD("loading false")
                    loading.onNext(false)
                }
                .share {
                    filterResultSuccess()
                            .map { it.map(::mapRepoToRepoItem) }
                            .bindSubject(repoes)
                            .addTo(viewIntent.subscriptions)

                    filterResultFailure()
                            .bindSubject(error)
                            .addTo(viewIntent.subscriptions)
                }

    }

    override fun detachView() {

    }

}

fun mapRepoToRepoItem(repo: RepoEntity) = RepoListContract.RepoItem(repo.id,
        repo.name,
        repo.description ?: "",
        repo.stargazers_count)