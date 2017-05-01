package com.github.judrummer.kithub.app.main.repolist

import com.github.judrummer.jxadapter.JxItem
import com.github.judrummer.kithub.base.BaseViewModel
import rx.Observable
import rx.Subscription
import rx.subscriptions.CompositeSubscription


interface RepoListContract {
    interface ViewIntent {
        val refreshIntent: Observable<Unit>
        val searchIntent: Observable<String>
    }

    interface ViewModel : BaseViewModel {
        val repos: Observable<List<RepoItem>>
        val loading: Observable<Boolean>
        val error: Observable<Exception>
    }

    data class RepoItem(val id: String = "", val name: String = "", val description: String = "", val starCount: Int = 0) : JxItem

}

