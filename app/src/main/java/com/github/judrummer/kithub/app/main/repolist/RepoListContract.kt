package com.github.judrummer.kithub.app.main.repolist

import com.github.judrummer.jxadapter.JxItem
import com.github.judrummer.kithub.base.BaseViewModel
import io.reactivex.Observable

interface RepoListContract {
    interface ViewIntent {
        val refreshIntent: Observable<Unit>
    }

    interface ViewModel : BaseViewModel<State> {
        val showError: Observable<Exception>
    }

    data class State(val repos: List<RepoItem> = listOf(), val loading: Boolean = false, val error: Exception? = null)

    data class RepoItem(val id: String = "", val name: String = "", val description: String = "", val starCount: Int = 0) : JxItem

}

