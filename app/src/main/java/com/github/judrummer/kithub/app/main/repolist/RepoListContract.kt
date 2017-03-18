package com.github.judrummer.kithub.app.main.repolist

import com.github.judrummer.kithub.base.BaseViewModel
import com.github.judrummer.kithub.data.usecase.RepoEntity
import rx.Observable
import rx.Subscription
import rx.subscriptions.CompositeSubscription


interface RepoListContract {
    interface ViewIntent {
        val refreshIntent: Observable<Unit>
        val searchIntent: Observable<String>
        val subscriptions: CompositeSubscription
    }

    interface ViewModel : BaseViewModel {
        val repoes: Observable<List<RepoItem>>
        val loading: Observable<Boolean>
        val error: Observable<Exception>
    }

    data class RepoItem(val id: String, val name: String, val description: String, val starCount: Int)

}

