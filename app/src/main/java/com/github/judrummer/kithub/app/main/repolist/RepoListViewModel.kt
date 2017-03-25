package com.github.judrummer.kithub.app.main.repolist

import com.github.judrummer.kithub.data.entity.RepoEntity
import com.github.judrummer.kithub.data.usecase.*
import com.github.judrummer.kithub.extension.bindSubject
import com.github.judrummer.kithub.extension.filterResultFailure
import com.github.judrummer.kithub.extension.filterResultSuccess
import com.github.judrummer.kithub.extension.share
import com.github.kittinunf.reactiveandroid.rx.addTo
import rx.Observable
import rx.subjects.PublishSubject
import rx.subscriptions.CompositeSubscription

class RepoListViewModel(
        private val viewIntent: RepoListContract.ViewIntent,
        private val getRepos: GetRepos = GetReposImpl,
        private val searchRepos: SearchRepos = SearchReposImpl
) : RepoListContract.ViewModel {

    override val repos = PublishSubject.create<List<RepoListContract.RepoItem>>()!!
    override val loading = PublishSubject.create<Boolean>()!!
    override val error = PublishSubject.create<Exception>()!!
    private val subscriptions = CompositeSubscription()
    override fun attachView() {
        Observable.combineLatest(viewIntent.refreshIntent, viewIntent.searchIntent) { _, search -> search }
                .doOnNext { loading.onNext(true) }
                .switchMap { search ->
                    if (search.isNotBlank()) {
                        searchRepos(search)
                    } else {
                        getRepos()
                    }
                }
                .doOnNext { loading.onNext(false) }
                .share {
                     filterResultSuccess()
                            .map { it.map(::mapRepoToRepoItem) }
                            .bindSubject(repos)
                            .addTo(subscriptions)

                    filterResultFailure()
                            .bindSubject(error)
                            .addTo(subscriptions)
                }

    }

    override fun detachView() {
        subscriptions.clear()
    }

}

fun mapRepoToRepoItem(repo: RepoEntity) = RepoListContract.RepoItem(repo.id,
        repo.name,
        repo.description ?: "",
        repo.stargazers_count)