package com.github.judrummer.kithub.app.main.repolist

import com.ezkae.common.extension.filterResultFailure
import com.ezkae.common.extension.filterResultSuccess
import com.ezkae.common.extension.rx_bindNext
import com.github.judrummer.kithub.data.model.Repo
import com.github.judrummer.kithub.data.repository.GithubRepoRepository
import com.github.kittinunf.reactiveandroid.MutableProperty
import com.github.kittinunf.reactiveandroid.rx.bindTo
import rx.Observable

class RepoListViewModel(private val view: RepoListContract.View, private val repository: GithubRepoRepository) : RepoListContract.ViewModel {

    private val repoesProperty = MutableProperty(listOf<Repo>())
    override val repoes: Observable<List<Repo>> = repoesProperty.observable

    override fun attachView() {
        Observable.combineLatest(view.refreshIntent(), view.searchIntent()) { refresh, search -> search }
                .doOnNext { view.showProgressBar() }
                .switchMap { repository.getRepoes() }
                .doOnNext { view.hideProgressBar() }
                .share()
                .apply {
                    filterResultSuccess().bindTo(repoesProperty)
                    filterResultFailure().rx_bindNext(view::showError)
                }
    }

    override fun detachView() {

    }

}