package com.github.judrummer.kithub.app.main.repolist

import com.github.judrummer.kithub.base.BaseViewModel
import com.github.judrummer.kithub.data.model.Repo
import rx.Observable

interface RepoListContract {
    interface View {
        fun refreshIntent(): Observable<Unit>
        fun searchIntent(): Observable<String>
        fun showProgressBar()
        fun hideProgressBar()
        fun showError(error: Throwable)
    }

    interface ViewModel : BaseViewModel {
        val repoes: Observable<List<Repo>>
    }
}

